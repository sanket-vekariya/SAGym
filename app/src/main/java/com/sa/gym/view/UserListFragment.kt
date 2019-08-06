package com.sa.gym.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sa.gym.R
import com.sa.gym.adapter.RecyclerViewAdapter
import com.sa.gym.model.UserItem
import com.sa.gym.viewModel.FireStoreRepository
import com.sa.gym.viewModel.FireStoreViewModel
import kotlinx.android.synthetic.main.fragment_user_list.*

class UserListFragment : Fragment() {

    private var m1Adapter: RecyclerViewAdapter? = null
    private val savedUserList: MutableList<UserItem> = mutableListOf()
    private val list: MutableList<UserItem> = mutableListOf()
    private var order = false
    private var isScrolling = false
    private var isLastItemReached = false
    private val limit: Long = 15
    private lateinit var lastVisible: DocumentSnapshot

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fireStoreViewModel: FireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel::class.java)

        val rootRef = FirebaseFirestore.getInstance()
        val productsRef = rootRef.collection("user")
        val query = productsRef.limit(limit)

        fireStoreViewModel.getUserData().observe(this, Observer {
            testRecyclerView.layoutManager = LinearLayoutManager(context)
            m1Adapter = RecyclerViewAdapter(list)
            testRecyclerView.adapter = m1Adapter
        })
        //if query is completely executed
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //adding user-item date into mutable list
                for (document in task.result!!) {
                    val productModel: UserItem = document.toObject(UserItem::class.java)
                    list.add(productModel)
                }
                m1Adapter?.notifyDataSetChanged()
            }
            lastVisible = task.result!!.documents[task.result!!.size() - 1]
        }
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        val rootRef = FirebaseFirestore.getInstance()
        val productsRef = rootRef.collection("user")

        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount

                if (isScrolling && firstVisibleItemPosition + visibleItemCount == totalItemCount && !isLastItemReached) {
                    isScrolling = false
                    val nextQuery = productsRef.startAfter(lastVisible).limit(limit)

                    nextQuery.get().addOnCompleteListener { t ->
                        if (t.isSuccessful) {
                            for (d in t.result!!) {
                                val userModel = d.toObject(UserItem::class.java)
                                list.add(userModel)
                            }
                            m1Adapter?.notifyDataSetChanged()
                            lastVisible = t.result!!.documents[t.result!!.size() - 1]
                            if (t.result!!.size() < limit) {
                                isLastItemReached = true
                                Toast.makeText(context, "All The Data Loaded", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        testRecyclerView.addOnScrollListener(onScrollListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //----------------------spinner selected filtering----------------------------------------
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            //if item is not selected
            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinner.setSelection(0)
            }

            //on item selected
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, int: Int, long: Long) {
                if (int != 0) {
                    customQuery(int)
                } else if (int == 0) {
                    m1Adapter = RecyclerViewAdapter(list)
                    testRecyclerView.adapter = m1Adapter
                }
            }
        }

        //-----------------------------------------filtering by below fields----------------------
        text_fix_name.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("firstName")
            image_name.rotation = image_name.rotation + 180
        }

        text_fix_in_time.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("inTime")
            image_in_time.rotation = image_in_time.rotation + 180
        }

        text_fix_out_time.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("outTime")
            image_out_time.rotation = image_out_time.rotation + 180
        }

        text_fix_due.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("paymentStatus")
            image_due.rotation = image_due.rotation + 180
        }

        //------------------------pull down to refresh logic-----------------------------------------
        testSwipeRefreshLayout.setOnRefreshListener {
            testSwipeRefreshLayout.setColorSchemeColors(Color.MAGENTA)
            testSwipeRefreshLayout.isRefreshing = false
            if (spinner.selectedItemPosition != 0)
                spinner.setSelection(0)

            resetRotation()
        }
    }

    //------------------------------reset rotation of filter images----------------------------------
    fun resetRotation() {
        image_due.rotation = 0f
        image_in_time.rotation = 0f
        image_name.rotation = 0f
        image_out_time.rotation = 0f
    }

    //---------------------------Query for month-wise user selection---------------------------------
    fun customQuery(month: Int) {
        FireStoreRepository().getSavedUser().whereEqualTo("month", month)
            .addSnapshotListener { value, _ ->
                savedUserList.clear()
                m1Adapter = testRecyclerView.adapter as RecyclerViewAdapter
                for (doc in value!!) {
                    val userItem = doc.toObject(UserItem::class.java)
                    savedUserList.add(userItem)
                }
                m1Adapter = RecyclerViewAdapter(savedUserList)
                testRecyclerView.adapter = m1Adapter
            }

    }

    //----------------------function for switching of ascending, descending query---------------------
    fun switchOrder(field: String) {
        if (!order) {
            queryAscending(field)
            order = true
        } else if (order) {
            queryDescending(field)
            order = false
        }
    }

    //---------------------------------------Query for month-wise user selection-----------------------
    fun queryAscending(field: String) {
        FirebaseFirestore.getInstance().collection("user").orderBy(field, Query.Direction.ASCENDING).get()
            .addOnSuccessListener {
                savedUserList.clear()
                for (doc in it!!) {
                    val userItem = doc.toObject(UserItem::class.java)
                    savedUserList.add(userItem)
                }
                m1Adapter = RecyclerViewAdapter(savedUserList)
                testRecyclerView.adapter = m1Adapter
            }
    }

    //---------------Query for month-wise user selection-------------------
    fun queryDescending(field: String) {
        FirebaseFirestore.getInstance().collection("user").orderBy(field, Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                savedUserList.clear()
                for (doc in it!!) {
                    val userItem = doc.toObject(UserItem::class.java)
                    savedUserList.add(userItem)
                }
                m1Adapter = RecyclerViewAdapter(savedUserList)
                testRecyclerView.adapter = m1Adapter
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        testRecyclerView.adapter = null
    }
}

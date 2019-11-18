package com.sa.gym.view

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.firebase.firestore.Query
import com.sa.gym.R
import com.sa.gym.adapter.RecyclerViewAdapter
import com.sa.gym.model.UserItem
import com.sa.gym.viewModel.FireStoreRepository
import com.sa.gym.viewModel.FireStoreViewModel
import com.sa.gym.viewModel.QueryViewModel

class UserListFragment : Fragment() {
    private var mAdapter: RecyclerViewAdapter? = null
    private val list: MutableList<UserItem> = mutableListOf()
    private val limit: Long = 15
    private var order = false
    private var isScrolling = false
    private var isLastItemReached = false
    private var firstVisibleItemPosition: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private lateinit var lastVisible: DocumentSnapshot
    private lateinit var fireStoreViewModel: FireStoreViewModel
    private lateinit var queryViewModel: QueryViewModel
    private lateinit var query: Query
    private lateinit var nextQuery: Query
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var userModel: UserItem
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel::class.java)
        queryViewModel = ViewModelProviders.of(this).get(QueryViewModel::class.java)
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        query = FireStoreRepository().getSavedUser().limit(limit)
        fireStoreViewModel.getUserData().observe(this, Observer {
            testRecyclerView.layoutManager = LinearLayoutManager(context)
            mAdapter = RecyclerViewAdapter(list)
            testRecyclerView.adapter = mAdapter
        })
        //---------------------------------Pagination Logic-------------------------------------
        //if query is completely executed
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //adding user-item date into mutable list
                for (document in task.result!!) {
                    val productModel: UserItem = document.toObject(UserItem::class.java)
                    list.add(productModel)
                }
                mAdapter?.notifyDataSetChanged()
            }
            lastVisible = task.result!!.documents[task.result!!.size() - 1]
        }
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                visibleItemCount = linearLayoutManager.childCount
                totalItemCount = linearLayoutManager.itemCount
                if (isScrolling && firstVisibleItemPosition + visibleItemCount == totalItemCount && !isLastItemReached) {
                    isScrolling = false
                    nextQuery =
                        FireStoreRepository().getSavedUser().startAfter(lastVisible).limit(limit)
                    nextQuery.get().addOnCompleteListener { t ->
                        if (t.isSuccessful) {
                            for (documentSnapshot in t.result!!) {
                                userModel = documentSnapshot.toObject(UserItem::class.java)
                                list.add(userModel)
                            }
                            mAdapter?.notifyDataSetChanged()
                            lastVisible = t.result!!.documents[t.result!!.size() - 1]
                            if (t.result!!.size() <= limit) {
                                isLastItemReached = true
                                Toast.makeText(context, "All The Data Loaded", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
        testRecyclerView.addOnScrollListener(onScrollListener)
    }

    //---------------------------------Searching Name Logic------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //searching in users
        edit_search_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                queryViewModel.searchUserByName(edit_search_name.text.toString())
                    .observe(this@UserListFragment, Observer {
                        mAdapter = RecyclerViewAdapter(it)
                        testRecyclerView.adapter = mAdapter
                    })
            }
        })
        //----------------------spinner selected filtering logic----------------------------------------
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            //if item is not selected
            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinner.setSelection(0)
            }

            //on item selected
            override fun onItemSelected(
                adapterView: AdapterView<*>,
                view: View,
                int: Int,
                long: Long
            ) {
                if (edit_search_name.text != null) {
                    edit_search_name.text = null
                }
                if (int != 0) {
                    queryViewModel.customQueryEquals("month", int)
                        .observe(this@UserListFragment, Observer {
                            mAdapter = RecyclerViewAdapter(it)
                            testRecyclerView.adapter = mAdapter
                        })
                } else if (int == 0) {
                    fireStoreViewModel.getUserData().observe(this@UserListFragment, Observer {
                        mAdapter = RecyclerViewAdapter(list)
                        testRecyclerView.adapter = mAdapter
                    })
                }
            }
        }
        //-----------------------------------------filtering by below fields----------------------
        text_fix_name.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("firstName")
            image_name.rotation = image_name.rotation + 180
        }
        image_name.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("firstName")
            image_name.rotation = image_name.rotation + 180
        }
        text_fix_in_time.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("inTime")
            image_in_time.rotation = image_in_time.rotation + 180
        }
        image_in_time.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("inTime")
            image_in_time.rotation = image_in_time.rotation + 180
        }
        text_fix_out_time.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("outTime")
            image_out_time.rotation = image_out_time.rotation + 180
        }
        image_out_time.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("outTime")
            image_out_time.rotation = image_out_time.rotation + 180
        }
        text_fix_due.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("paymentStatus")
            image_due.rotation = image_due.rotation + 180
        }
        image_due.setOnClickListener {
            spinner.setSelection(0)
            switchOrder("paymentStatus")
            image_due.rotation = image_due.rotation + 180
        }
        //------------------------pull down to refresh logic-----------------------------------------
        testSwipeRefreshLayout.setOnRefreshListener {
            testSwipeRefreshLayout.setColorSchemeColors(Color.MAGENTA)
            edit_search_name.text = null
            testSwipeRefreshLayout.isRefreshing = false
            if (spinner.selectedItemPosition != 0)
                spinner.setSelection(0)
            resetRotation()
        }
    }

    //------------------------------reset rotation of filter images----------------------------------
    private fun resetRotation() {
        image_due.rotation = 0f
        image_in_time.rotation = 0f
        image_name.rotation = 0f
        image_out_time.rotation = 0f
    }

    //----------------------function for switching of ascending, descending query---------------------
    private fun switchOrder(field: String) {
        if (!order) {
            queryViewModel.queryAscending(field).observe(this@UserListFragment, Observer {
                mAdapter = RecyclerViewAdapter(it)
                testRecyclerView.adapter = mAdapter
            })
            order = true
        } else if (order) {
            queryViewModel.queryDescending(field).observe(this@UserListFragment, Observer {
                mAdapter = RecyclerViewAdapter(it)
                testRecyclerView.adapter = mAdapter
            })
            order = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        testRecyclerView.adapter = null
    }
}
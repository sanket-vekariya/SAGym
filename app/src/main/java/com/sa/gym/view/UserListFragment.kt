package com.sa.gym.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sa.gym.R
import com.sa.gym.adapter.RecyclerViewAdapter
import com.sa.gym.model.UserItem
import com.sa.gym.viewModel.FireStoreRepository
import com.sa.gym.viewModel.FireStoreViewModel
import kotlinx.android.synthetic.main.fragment_user_list.*


class UserListFragment : Fragment() {

    private var mAdapter: RecyclerViewAdapter? = null
    private var m1Adapter: RecyclerViewAdapter? = null
    private val savedUserList: MutableList<UserItem> = mutableListOf()
    private var order = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fireStoreViewModel: FireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel::class.java)

        fireStoreViewModel.getUserData().observe(this, Observer {
            testRecyclerView.layoutManager = LinearLayoutManager(context)
            mAdapter = RecyclerViewAdapter(it)
            testRecyclerView.itemAnimator = DefaultItemAnimator()
            testRecyclerView.adapter = mAdapter
        })

        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //----------------------spinner selected filtering----------------------------------------
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            //if item is not selected
            override fun onNothingSelected(parent: AdapterView<*>?) {
                testRecyclerView.adapter = mAdapter
            }

            //on item selected
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, int: Int, long: Long) {
                if (int != 0) {
                    customQuery(int)
                } else if (int == 0)
                    testRecyclerView.adapter = mAdapter
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

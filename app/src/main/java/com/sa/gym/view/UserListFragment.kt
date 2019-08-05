package com.sa.gym.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fireStoreViewModel: FireStoreViewModel = ViewModelProviders.of(this).get(FireStoreViewModel::class.java)

        fireStoreViewModel.getUserData().observe(this, Observer {
            testRecyclerView.layoutManager = LinearLayoutManager(context)
            mAdapter = RecyclerViewAdapter(it)
            testRecyclerView.adapter = mAdapter
        })

        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //spinner selected filtering
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

        //pull down to refresh logic
        testSwipeRefreshLayout.setOnRefreshListener {
            testSwipeRefreshLayout.isRefreshing = false
            if (spinner.selectedItemPosition != 0)
                spinner.setSelection(0)
            testRecyclerView.invalidate()
        }
    }

    //Query for month-wise user selection
    fun customQuery(month: Int) {
        FireStoreRepository().getSavedUser().whereEqualTo("month", month)
            .addSnapshotListener { value, _ ->
                savedUserList.clear()
                for (doc in value!!) {
                    val userItem = doc.toObject(UserItem::class.java)
                    savedUserList.add(userItem)
                }
                m1Adapter = RecyclerViewAdapter(savedUserList)
                testRecyclerView.adapter = m1Adapter
            }
    }
}

package com.sa.gym.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sa.gym.R
import com.sa.gym.adapter.RecyclerViewAdapter
import com.sa.gym.viewModel.FirestoreViewModel
import kotlinx.android.synthetic.main.fragment_sample.*


class SampleFragment : Fragment() {

    private var mAdapter: RecyclerViewAdapter? = null

    val TAG = "main"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val firestoreViewModel: FirestoreViewModel = ViewModelProviders.of(this).get(FirestoreViewModel::class.java)

        firestoreViewModel.getSavedAddresses().observe(this, Observer { it ->
            testRecyclerView.layoutManager = LinearLayoutManager(context)
            mAdapter = RecyclerViewAdapter(it)
            testRecyclerView.adapter = mAdapter
            Log.e("main", "data observed ")
        })
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testSwipeRefreshLayout.setOnRefreshListener {
            testSwipeRefreshLayout.isRefreshing = false
            testRecyclerView.adapter = mAdapter
            testRecyclerView.invalidate()
        }
    }
}

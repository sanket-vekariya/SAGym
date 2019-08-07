package com.sa.gym.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sa.gym.R
import com.sa.gym.viewModel.QueryViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {
    private lateinit var queryViewModel: QueryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for live change in dashboard
        queryViewModel = ViewModelProviders.of(this).get(QueryViewModel::class.java)
        queryViewModel.customQueryAmount().observe(this@DashboardFragment, Observer {
            text_member_count.text = it[0].toString()
            text_member_due_done.text = it[1].toString()
            text_member_due_pending.text = it[2].toString()
            text_member_active.text = it[3].toString()
            text_member_inactive.text = it[4].toString()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }
}
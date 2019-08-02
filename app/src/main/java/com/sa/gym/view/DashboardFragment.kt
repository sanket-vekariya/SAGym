package com.sa.gym.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sa.gym.R
import com.sa.gym.model.UserItem
import com.sa.gym.viewModel.FirestoreRepository
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlin.math.sqrt


class DashboardFragment : Fragment() {
    companion object {
        var count = ""
        var total: Long = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirestoreRepository().getSavedUser()
            .addSnapshotListener { value, _ ->
                count = value?.count().toString()
                customActive()
                customQuery()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    //Query for Paid, Pending Due Amount
    private fun customQuery() {
        FirestoreRepository().getSavedUser().get().addOnCompleteListener { task ->
            var totalPending: Long = 0
            var totalDone: Long = 0
            var totalDoc: Long = 0
            if (task.isSuccessful) {
                for (doc in task.result!!) {
                    val userItem = doc.toObject(UserItem::class.java)
                    if (!userItem.paymentStatus) {
                        val itemCost: Long = doc.getLong("amount") as Long
                        totalPending += itemCost
                    }
                    if (userItem.paymentStatus) {
                        val itemCost: Long = doc.getLong("amount") as Long
                        totalDone += itemCost
                    }
                    totalDoc += task.result!!.count()
                }
            }
            try {

                total = sqrt(totalDoc.toDouble()).toLong()
                text_member_count.setText(total.toString(), TextView.BufferType.EDITABLE)
                text_member_due_pending.text = totalPending.toString()
                text_member_due_done.text = totalDone.toString()
            } catch (e: NullPointerException) {
                e.stackTrace
            }
        }
    }

    //Query for Active, Inactive Members
    private fun customActive() {
        FirestoreRepository().getSavedUser().whereEqualTo("active", true).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var totalActive: Long = 0

                for (doc in task.result!!) {
                    totalActive += task.result!!.count()
                }
                try {
                    text_member_active.text = sqrt(totalActive.toDouble()).toLong().toString()
                    text_member_inactive.text = (total - sqrt(totalActive.toDouble())).toLong().toString()
                } catch (e: IllegalStateException) {
                    e.stackTrace
                }
            }
        }
    }
}
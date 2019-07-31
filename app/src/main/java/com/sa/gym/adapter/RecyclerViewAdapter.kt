package com.sa.gym.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sa.gym.R
import com.sa.gym.model.UserItem
import kotlinx.android.synthetic.main.custom_view.view.*

class RecyclerViewAdapter(val userItem: List<UserItem>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.custom_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = userItem[position].firstName
        holder.intime.text = userItem[position].inTime
        holder.outtime.text = userItem[position].outTime
        if (userItem[position].amount > 0)
            holder.due.setImageResource(R.drawable.reddot)
        else
            holder.due.setImageResource(R.drawable.greendot)
    }

    override fun getItemCount(): Int {
        return userItem.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.text_name
        val intime = itemView.text_intime
        val outtime = itemView.text_outtime
        val due = itemView.view_due
    }
}
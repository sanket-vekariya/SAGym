package com.sa.gym.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sa.gym.R
import com.sa.gym.model.UserItem
import kotlinx.android.synthetic.main.list_custom_user.view.*


class RecyclerViewAdapter(private val userItem: List<UserItem>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_custom_user, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = userItem[position].firstName
        holder.intime.text = userItem[position].inTime
        holder.outtime.text = userItem[position].outTime
        if (!userItem[position].paymentStatus)
            holder.due.setImageResource(R.drawable.ic_red_dot)
        else
            holder.due.setImageResource(R.drawable.ic_green_dot)
    }

    override fun getItemCount(): Int {
        return userItem.size
    }


    //view holder class
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.text_name
        val intime: TextView = itemView.text_in_time
        val outtime: TextView = itemView.text_out_time
        val due: ImageView = itemView.view_due
    }
}
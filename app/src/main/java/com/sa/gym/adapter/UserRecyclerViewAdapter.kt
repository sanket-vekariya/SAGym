package com.sa.gym.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sa.gym.R
import com.sa.gym.model.User
import com.sa.gym.view.UserActivity

class UserRecyclerViewAdapter(private val userList: MutableList<User>, private val context: Context, private val firestoreDB: FirebaseFirestore)
    : RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]

        holder.firstName.text = user.firstName
        holder.lastName.text = user.lastName
        holder.active.text = user.active
        holder.email.text = user.email

        holder.itemView.setOnClickListener {
            updateUser(user)
        }

        holder.delete.setOnClickListener {  deleteUser(user.id!!, position) }
    }


    override fun getItemCount(): Int {
        return userList.size
    }


    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var firstName: TextView = view.findViewById(R.id.text_firstname)
        internal var lastName: TextView = view.findViewById(R.id.text_lastname)
        internal var active: TextView = view.findViewById(R.id.text_active)
        internal var email: TextView = view.findViewById(R.id.text_email)
        internal var delete: ImageView = view.findViewById(R.id.ivDelete)
    }


    private fun updateUser(user: User) {
        val intent = Intent(context, UserActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("UpdateUserId", user.id)
        intent.putExtra("UpdateUserFirstName", user.firstName)
        intent.putExtra("UpdateUserLastName", user.lastName)
        intent.putExtra("UpdateUserActive", user.active)
        intent.putExtra("UpdateUserEmail", user.email)

        intent.putExtra("UpdateUserMobile", user.mobile)
        intent.putExtra("UpdateUserInTime", user.inTime)
        intent.putExtra("UpdateUserOutTime", user.outTime)

        intent.putExtra("UpdateUserAddress", user.address)
        intent.putExtra("UpdateUserDOB", user.birthDate)
        intent.putExtra("UpdateUserHeight", user.height!!.toFloat())
        intent.putExtra("UpdateUserWeight", user.weight)

        intent.putExtra("UpdateUserMembershipType", user.membershipType)
        intent.putExtra("UpdateUserAmount", user.amount)
        intent.putExtra("UpdateUserPaymentStatus", user.paymentStatus)
        intent.putExtra("UpdateUserAddedBy", user.addedBy)

        intent.putExtra("ButtonText","Update")
        context.startActivity(intent)
    }

    private fun deleteUser(id: String, position: Int) {
        firestoreDB.collection("user")
            .document(id)
            .delete()
            .addOnCompleteListener {
                userList.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, userList.size)
                Toast.makeText(context, "User has been deleted!", Toast.LENGTH_SHORT).show()
            }
    }
}
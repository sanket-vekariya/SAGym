package com.sa.gym.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.sa.gym.R
import com.sa.gym.adapter.UserRecyclerViewAdapter
import com.sa.gym.model.User
import kotlinx.android.synthetic.main.fragment_user_list.*


class UserListFragment : Fragment() {
    private val TAG = "FireStoreDemoActivity"
    private var mAdapter: UserRecyclerViewAdapter? = null
    private var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadUserList()
        return inflater.inflate(R.layout.fragment_user_list, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestoreDB = FirebaseFirestore.getInstance()

        simpleSwipeRefreshLayout.setOnRefreshListener {
            simpleSwipeRefreshLayout.isRefreshing = false
            rvUserList.adapter = mAdapter
            rvUserList.invalidate()
        }


        firestoreListener = firestoreDB!!.collection("user")
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Listen failed!", e)
                    return@EventListener
                }

                val userList = mutableListOf<User>()

                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val user = doc.toObject(User::class.java)
                        user.id = doc.id
                        userList.add(user)
                    }
                }

                mAdapter = UserRecyclerViewAdapter(userList, activity!!.baseContext, firestoreDB!!)
                rvUserList.adapter = mAdapter
            })
    }


    override fun onDestroy() {
        super.onDestroy()

        firestoreListener!!.remove()
    }

    private fun loadUserList() {
        FirebaseFirestore.getInstance().collection("user")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userList = mutableListOf<User>()

                    for (doc in task.result!!) {
                        val user = doc.toObject(User::class.java)
                        user.id = doc.id
                        userList.add(user)
                    }

                    mAdapter = UserRecyclerViewAdapter(userList, activity!!.baseContext, firestoreDB!!)
                    val mLayoutManager = LinearLayoutManager(context)
                    rvUserList.layoutManager = mLayoutManager
                    rvUserList.itemAnimator = DefaultItemAnimator()
                    rvUserList.adapter = mAdapter
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }
}

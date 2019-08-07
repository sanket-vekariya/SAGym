package com.sa.gym.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.sa.gym.model.UserItem
import kotlin.math.sqrt

class QueryViewModel : ViewModel() {
    private var fireBaseRepository = FireStoreRepository()
    private var list: MutableLiveData<List<UserItem>> = MutableLiveData()
    private var listLong: MutableLiveData<List<Long>> = MutableLiveData()

    fun searchUserByName(searchText: String): LiveData<List<UserItem>> {
        fireBaseRepository.getSavedUser().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                list.value = null
                return@EventListener
            }

            val listValue: MutableList<UserItem> = mutableListOf()
            for (doc in value!!) {
                val userItem = doc.toObject(UserItem::class.java)
                if (userItem.firstName.contains(searchText, true)) {
                    listValue.add(userItem)
                }
            }
            list.value = listValue
        })
        return list
    }

    //---------------------------------------Query ascending------------------------------------
    fun queryAscending(field: String): LiveData<List<UserItem>> {
        fireBaseRepository.getSavedUserAscending(field).get().addOnSuccessListener {

            val listValue: MutableList<UserItem> = mutableListOf()
            for (doc in it!!) {
                val userItem = doc.toObject(UserItem::class.java)
                listValue.add(userItem)
            }
            list.value = listValue
        }
        return list
    }

    //---------------------------------------Query Descending------------------------------------
    fun queryDescending(field: String): LiveData<List<UserItem>> {
        fireBaseRepository.getSavedUserDescending(field).get().addOnSuccessListener {

            val listValue: MutableList<UserItem> = mutableListOf()
            for (doc in it!!) {
                val userItem = doc.toObject(UserItem::class.java)
                listValue.add(userItem)
            }
            list.value = listValue
        }
        return list
    }

    //---------------------------Query for intValue-wise user selection---------------------------------
    fun customQueryEquals(field: String, intValue: Int): LiveData<List<UserItem>> {
        fireBaseRepository.getSavedUserEquals(field, intValue).addSnapshotListener { value, _ ->

            val listValue: MutableList<UserItem> = mutableListOf()
            for (doc in value!!) {
                val userItem = doc.toObject(UserItem::class.java)
                listValue.add(userItem)
            }
            list.value = listValue
        }
        return list
    }

    //---------------------------Query for Paid, Pending Due Amount--------------------------------------
    fun customQueryAmount(): LiveData<List<Long>> {
        fireBaseRepository.getSavedUser().addSnapshotListener { value, _ ->
            var totalPending: Long = 0
            var totalDone: Long = 0
            var totalDoc: Long = 0
            var totalActive: Long = 0
            var totalInactive: Long = 0

            for (doc in value!!) {
                val userItem:UserItem = doc.toObject(UserItem::class.java)
                if (!userItem.paymentStatus) {
                    val itemCost = doc.getLong("amount") as Long
                    totalPending += itemCost
                }
                else if (userItem.paymentStatus) {
                    val itemCost = doc.getLong("amount") as Long
                    totalDone += itemCost
                }

                if (!userItem.active) {
                    totalInactive += 1
                }
                else if (userItem.active) {
                    totalActive += 1
                }
                totalDoc += value.count()
            }
            totalDoc = sqrt(totalDoc.toDouble()).toLong()

            val listValue: MutableList<Long> = mutableListOf(totalDoc, totalDone, totalPending, totalActive, totalInactive)
            listValue.add(0, totalDoc)
            listValue.add(1, totalDone)
            listValue.add(2, totalPending)
            listValue.add(3, totalActive)
            listValue.add(4, totalInactive)

            listLong.value = listValue
        }
        return listLong
    }

}
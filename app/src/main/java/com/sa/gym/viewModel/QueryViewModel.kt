package com.sa.gym.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.sa.gym.model.UserItem

class QueryViewModel : ViewModel() {
    private var fireBaseRepository = FireStoreRepository()
    private var savedUser: MutableLiveData<List<UserItem>> = MutableLiveData()
    private var list: MutableLiveData<List<UserItem>> = MutableLiveData()


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
    fun customQueryEquals(field : String, intValue: Int) : LiveData<List<UserItem>>{
        fireBaseRepository.getSavedUserEquals(field,intValue).addSnapshotListener { value, _ ->

            val listValue: MutableList<UserItem> = mutableListOf()
            for (doc in value!!) {
                    val userItem = doc.toObject(UserItem::class.java)
                    listValue.add(userItem)
                }
                list.value = listValue
            }
        return  list
    }



}
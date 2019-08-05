package com.sa.gym.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.sa.gym.model.UserItem

class FireStoreViewModel : ViewModel() {

    private var fireBaseRepository = FireStoreRepository()
    private var savedUser: MutableLiveData<List<UserItem>> = MutableLiveData()

    // save user to fireBase
    fun saveUserToFireBase(userItem: UserItem) {
        fireBaseRepository.saveUserItem(userItem).addOnFailureListener {
        }.addOnCompleteListener {
        }
    }

    // get real-time updates from FireBase regarding saved users
    fun getUserData(): LiveData<List<UserItem>> {
        fireBaseRepository.getSavedUser().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                savedUser.value = null
                return@EventListener
            }

            val savedUserList: MutableList<UserItem> = mutableListOf()
            for (doc in value!!) {
                val userItem = doc.toObject(UserItem::class.java)
                savedUserList.add(userItem)
            }
            savedUser.value = savedUserList
        })

        return savedUser
    }

}
package com.sa.gym.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.sa.gym.model.UserItem

class FirestoreViewModel : ViewModel() {

    var firebaseRepository = FirestoreRepository()
    var savedUser: MutableLiveData<List<UserItem>> = MutableLiveData()

    // save user to firebase
    fun saveUserToFirebase(userItem: UserItem) {
        firebaseRepository.saveUserItem(userItem).addOnFailureListener {
        }.addOnCompleteListener {
        }
    }

    // get realtime updates from firebase regarding saved users
    fun getUserData(): LiveData<List<UserItem>> {
        firebaseRepository.getSavedUser().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
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
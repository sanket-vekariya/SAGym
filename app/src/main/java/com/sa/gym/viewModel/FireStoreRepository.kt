package com.sa.gym.viewModel

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sa.gym.model.UserItem

class FireStoreRepository {

    private var fireStoreDB = FirebaseFirestore.getInstance()

    // save user to FireBase
    fun saveUserItem(userItem: UserItem): Task<Void> {
        val documentReference = fireStoreDB.collection("user").document()
        return documentReference.set(userItem)
    }

    // get saved users from FireBase
    fun getSavedUser(): CollectionReference {
        return fireStoreDB.collection("user")
    }
}
package com.sa.gym.viewModel

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.sa.gym.model.UserItem
class FirestoreRepository {

    var firestoreDB = FirebaseFirestore.getInstance()

    // save user to firebase
    fun saveUserItem(userItem: UserItem): Task<Void> {
        val documentReference = firestoreDB.collection("user").document()
        return documentReference.set(userItem)
    }

    // get saved users from firebase
    fun getSavedUser(): CollectionReference {
        return firestoreDB.collection("user")
    }
}
package com.sa.gym.viewModel

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    // get saved user in ascending order
    fun getSavedUserAscending(field : String) : Query{
        return fireStoreDB.collection("user").orderBy(field, Query.Direction.ASCENDING)
    }

    // get saved user in descencing order
    fun getSavedUserDescending(field : String) : Query{
        return fireStoreDB.collection("user").orderBy(field, Query.Direction.DESCENDING)
    }

    // get saved user with field matches with int value
    fun getSavedUserEquals(field : String, value : Int) : Query{
        return fireStoreDB.collection("user").whereEqualTo(field,value)
    }
}
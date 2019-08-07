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

    fun getSavedUserAscending(field : String) : Query{
        return fireStoreDB.collection("user").orderBy(field, Query.Direction.ASCENDING)
    }

    fun getSavedUserDescending(field : String) : Query{
        return fireStoreDB.collection("user").orderBy(field, Query.Direction.DESCENDING)
    }

    fun getSavedUserEquals(field : String, value : Int) : Query{
        return fireStoreDB.collection("user").whereEqualTo(field,value)
    }

}
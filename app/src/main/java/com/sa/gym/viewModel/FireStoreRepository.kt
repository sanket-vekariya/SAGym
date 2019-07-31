package com.sa.gym.viewModel

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.sa.gym.model.UserItem

class FirestoreRepository {

    var firestoreDB = FirebaseFirestore.getInstance()

    // save address to firebase
    fun saveAddressItem(addressItem: UserItem): Task<Void> {
        val documentReference = firestoreDB.collection("user").document()
        return documentReference.set(addressItem)
    }

    // get saved addresses from firebase
    fun getSavedAddress(): CollectionReference {
        return firestoreDB.collection("user")
    }

    //delete addresses from firebase
    fun deleteAddress(addressItem: UserItem): Task<Void> {
        val documentReference = firestoreDB.collection("user").document()
        return documentReference.delete()
    }

}
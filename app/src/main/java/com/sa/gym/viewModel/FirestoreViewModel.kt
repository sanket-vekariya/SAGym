package com.sa.gym.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.sa.gym.model.UserItem

class FirestoreViewModel : ViewModel() {

    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepository = FirestoreRepository()
    var savedAddresses: MutableLiveData<List<UserItem>> = MutableLiveData()

    // save address to firebase
    fun saveAddressToFirebase(addressItem: UserItem) {
        firebaseRepository.saveAddressItem(addressItem).addOnFailureListener {
            Log.e(TAG, "Failed to save Address!")
        }.addOnCompleteListener {
            Log.e(TAG, "Save data Address Successfully!")
        }
    }

    // get realtime updates from firebase regarding saved addresses
    fun getSavedAddresses(): LiveData<List<UserItem>> {
        firebaseRepository.getSavedAddress().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                savedAddresses.value = null
                return@EventListener
            }

            var savedAddressList: MutableList<UserItem> = mutableListOf()
            for (doc in value!!) {
                var addressItem = doc.toObject(UserItem::class.java)
                savedAddressList.add(addressItem)
                Log.e(TAG,"Load data successfully ${savedAddresses.value}")
            }
            savedAddresses.value = savedAddressList
        })

        return savedAddresses
    }

    // delete an address from firebase
    fun deleteAddress(addressItem: UserItem) {
        firebaseRepository.deleteAddress(addressItem).addOnFailureListener {
            Log.e(TAG, "Failed to delete Address")
        }.addOnCompleteListener {
            Log.e(TAG, "Delete Address Success!")
        }
    }

}
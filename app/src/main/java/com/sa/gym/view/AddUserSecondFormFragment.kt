package com.sa.gym.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.sa.gym.R
import com.sa.gym.model.UserItem
import com.sa.gym.viewModel.FirestoreViewModel
import kotlinx.android.synthetic.main.fragment_form_add_user_second.*


class AddUserSecondFormFragment : Fragment() {

    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_form_add_user_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestoreDB = FirebaseFirestore.getInstance()
        val firestoreViewModel: FirestoreViewModel = ViewModelProviders.of(this).get(FirestoreViewModel::class.java)

        val mBundle: Bundle? = arguments
        val firstName: String = mBundle?.getString("firstName", "firstName") as String
        val lastName: String = mBundle.getString("lastName", "lastName") as String
        val active: Boolean = mBundle.getBoolean("active", false)
        val email: String = mBundle.getString("email", "name@gmail.com") as String
        val contact: Long = mBundle.getLong("contact", 9512373997)
        val intime: String = mBundle.getString("intime", "09:30") as String
        val outtime: String = mBundle.getString("outtime", "10:30") as String
        val dob: String = mBundle.getString("dob", "08/08/1998") as String
        val address: String = mBundle.getString("address", "Ahmedabad") as String

        //button click user add on cloud
        button_add.setOnClickListener {
            firestoreViewModel.saveUserToFirebase(
                UserItem(
                    id,
                    firstName, lastName, active, email, contact, intime, outtime, address, dob,
                    edit_height.text.toString().toFloat(),
                    edit_weight.text.toString().toFloat(),
                    edit_membership_type.text.toString(),
                    edit_amount.text.toString().toInt(),
                    edit_payment_status.text.toString().toBoolean(),
                    edit_added_by.text.toString()
                )
            )
            activity?.finish()
        }

        //back button
        button_back.setOnClickListener {
            super.getActivity()?.onBackPressed()
        }

    }
}
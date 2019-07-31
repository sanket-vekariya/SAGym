package com.sa.gym.view

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.sa.gym.R
import com.sa.gym.model.User
import com.sa.gym.model.UserItem
import com.sa.gym.viewModel.FirestoreViewModel
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        firestoreDB = FirebaseFirestore.getInstance()
        val firestoreViewModel: FirestoreViewModel = ViewModelProviders.of(this).get(FirestoreViewModel::class.java)


        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("UpdateUserId")
            edit_firstname.setText(bundle.getString("UpdateUserFirstName"))
            edit_lastname.setText(bundle.getString("UpdateUserLastName"))
            edit_active.setText(bundle.getString("UpdateUserActive"))
            edit_email.setText(bundle.getString("UpdateUserEmail"))

            edit_contact.setText(bundle.getString("UpdateUserMobile"))
            edit_intime.setText(bundle.getString("UpdateUserInTime"))
            edit_outtime.setText(bundle.getString("UpdateUserOutTime"))

            edit_address.setText(bundle.getString("UpdateUserAddress"))
            edit_dob.setText(bundle.getString("UpdateUserDOB"))
            edit_height.setText(bundle.getString("UpdateUserHeight"))
            edit_weight.setText(bundle.getString("UpdateUserWeight"))

            edit_membership_type.setText(bundle.getString("UpdateUserMembershipType"))
            edit_amount.setText(bundle.getString("UpdateUserAmount"))
            edit_payment_status.setText(bundle.getString("UpdateUserPaymentStatus"))
            edit_added_by.setText(bundle.getString("UpdateUserAddedBy"))

            button_add.text = bundle.getString("ButtonText")
        }

        button_add.setOnClickListener {


            val firstName: String = edit_firstname.text.toString()
            val lastName: String = edit_lastname.text.toString()
            val active: String = edit_active.text.toString()
            val email: String = edit_email.text.toString()

            val contact: Long = edit_contact.text.toString().toLong()
            val intime: String = edit_intime.text.toString()
            val outtime: String = edit_outtime.text.toString()

            val address: String = edit_address.text.toString()
            val dob: String = edit_dob.text.toString()
            val height: Float = edit_height.text.toString().toFloat()
            val weight: Float = edit_weight.text.toString().toFloat()

            val membershipType: String = edit_membership_type.text.toString()
            val amount: Int = edit_amount.text.toString().toInt()
            val paymentStatus: Boolean = edit_payment_status.text.toString().toBoolean()
            val addedBy: String = edit_added_by.text.toString()


            firestoreViewModel.saveAddressToFirebase(
                UserItem(
                    id,
                    firstName,
                    lastName,
                    active,
                    email,
                    contact,
                    intime,
                    outtime,
                    address,
                    dob,
                    height,
                    weight,
                    membershipType,
                    amount,
                    paymentStatus,
                    addedBy
                )
            )

//            if (firstName.isNotEmpty()) {
//                if (id.isNotEmpty()) {
//                    updateUser(
//                        id,
//                        firstName,
//                        lastName,
//                        active,
//                        email,
//                        contact,
//                        intime,
//                        outtime,
//                        address,
//                        dob,
//                        height,
//                        weight,
//                        membershipType,
//                        amount,
//                        paymentStatus,
//                        addedBy
//                    )
//
//                } else {
//
//                    addUser(
//                        firstName,
//                        lastName,
//                        active,
//                        email,
//                        contact,
//                        intime,
//                        outtime,
//                        address,
//                        dob,
//                        height,
//                        weight,
//                        membershipType,
//                        amount,
//                        paymentStatus,
//                        addedBy
//                    )
//                }
//            }

            finish()
        }
    }

    private fun updateUser(
        id: String,
        firstName: String,
        lastName: String,
        active: String,
        email: String,
        contact: Long,
        intime: String,
        outtime: String,
        address: String,
        dob: String,
        height: Float,
        weight: Float,
        membershipType: String,
        amount: Int,
        paymentStatus: Boolean,
        addedBy: String
    ) {
        val user = User(
            id,
            firstName,
            lastName,
            active,
            email,
            contact,
            intime,
            outtime,
            address,
            dob,
            height,
            weight,
            membershipType,
            amount,
            paymentStatus,
            addedBy
        ).toMap()

        firestoreDB!!.collection("user")
            .document(id)
            .update(user)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "User has been updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "User could not be updated!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addUser(
        firstName: String,
        lastName: String,
        active: String,
        email: String,
        contact: Long,
        intime: String,
        outtime: String,
        address: String,
        dob: String,
        height: Float,
        weight: Float,
        membershipType: String,
        amount: Int,
        paymentStatus: Boolean,
        addedBy: String
    ) {
        val user = User(
            firstName,
            lastName,
            active,
            email,
            contact,
            intime,
            outtime,
            address,
            dob,
            height,
            weight,
            membershipType,
            amount,
            paymentStatus,
            addedBy
        ).toMap()

        firestoreDB!!.collection("user")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "User has been added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "User could not be added!", Toast.LENGTH_SHORT).show()
            }
    }
}
package com.sa.gym.view

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.sa.gym.R
import com.sa.gym.model.User
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        firestoreDB = FirebaseFirestore.getInstance()

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("UpdateUserId").toString()
            edit_firstname.setText(bundle.getString("UpdateUserFirstName"))
            edit_lastname.setText(bundle.getString("UpdateUserLastName"))
            edit_active.setText(bundle.getString("UpdateUserActive"))
            edit_email.setText(bundle.getString("UpdateUserEmail"))
            button_add.text = bundle.getString("ButtonText")
        }

        button_add.setOnClickListener {
            val firstName = edit_firstname.text.toString()
            val lastName = edit_lastname.text.toString()
            val active = edit_active.text.toString()
            val email = edit_email.text.toString()

            if (firstName.isNotEmpty()) {
                if (id.isNotEmpty()) {
                    updateUser(id, firstName, lastName, active, email)
                } else {
                    addUser(firstName, lastName, active, email)
                }
            }

            finish()
        }
    }

    private fun updateUser(id: String, firstName: String, lastName: String, active: String, email: String) {
        val user = User(id, firstName, lastName, active, email).toMap()

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

    private fun addUser(firstName: String, lastName: String, active: String, email: String) {
        val user = User(firstName, lastName, active, email).toMap()

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
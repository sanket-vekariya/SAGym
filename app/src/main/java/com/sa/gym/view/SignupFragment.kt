package com.sa.gym.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        FirebaseApp.initializeApp(activity!!.applicationContext)

        //signup button click add details in Firestore "admin" collection
        button_signup.setOnClickListener {
            val email = edit_email_signup.text.toString()
            val password = edit_password_signup.text.toString()

            when {
                edit_email_signup.text.toString().isEmpty() -> edit_email_signup.error = "insert email first"
                edit_password_signup.text.toString().isEmpty() -> edit_password_signup.error = "insert password first"
                else -> //create user with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val transaction = requireFragmentManager().beginTransaction()
                                transaction.replace(R.id.container, LoginFragment()).commit()
                                emailValidation()
                            } else {
                                edit_email_signup.error = "insert email properly"
                            }
                        }
            }
        }

    }
    private fun emailValidation() {
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Verification email sent to " + FirebaseAuth.getInstance().currentUser?.email,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

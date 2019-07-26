package com.sa.gym.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import com.sa.gym.viewModel.SignupViewModel
import kotlinx.android.synthetic.main.fragment_signup.*


class SignupFragment : Fragment() {

    companion object {
        fun newInstance() = SignupFragment()
    }

    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        FirebaseApp.initializeApp(context)

        //signup button click add details in Firestore "admin" collection
        button_signup.setOnClickListener {
            val email = edit_email_signup.text.toString()
            val password = edit_password_signup.text.toString()

            if (edit_email_signup.text.toString().isNullOrEmpty())
                edit_email_signup.error = "insert email first"
            else if (edit_password_signup.text.toString().isNullOrEmpty())
                edit_password_signup.error = "insert password first"
            else {
                //create user with email and password
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val transaction = requireFragmentManager().beginTransaction()
                            transaction.replace(R.id.container, LoginFragment()).commit()
                            emailValidation()
                            Log.d("main", "register button clicked")
                        } else {
                            edit_email_signup.error = "insert email properly"
                        }
                    }
            }

            viewModel = ViewModelProviders.of(this).get(
                SignupViewModel::
                class.java
            )
        }

    }
    fun emailValidation() {
        FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Verification email sent to " + FirebaseAuth.getInstance().currentUser?.email,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e("main", "sendEmailVerification failed!", task.exception)
                    Toast.makeText(
                        context,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

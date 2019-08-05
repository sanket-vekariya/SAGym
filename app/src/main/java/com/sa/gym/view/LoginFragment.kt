package com.sa.gym.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //login screen open
        button_login.setOnClickListener {
            when {
                edit_email_login.text.toString().isEmpty() -> edit_email_login.error =
                    getString(R.string.insert_email_first)
                edit_password_login.text.toString().isEmpty() -> edit_password_login.error =
                    getString(R.string.insert_password_first)
                else -> authentication(edit_email_login.text.toString(), edit_password_login.text.toString())
            }
        }

        //forget ic_password
        text_forget_password.setOnClickListener {
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, ForgetPasswordFragment()).addToBackStack(null).commit()
        }

        //sign-up screen open
        text_sign_up.setOnClickListener {
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, SignUpFragment()).addToBackStack(null).commit()
        }
    }

    //login authentication with email and ic_password
    private fun authentication(email: String, password: String) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                    Toast.makeText(context, getString(R.string.login_success), Toast.LENGTH_LONG).show()
                    startActivity(Intent(context, DashboardActivity::class.java))
                    activity?.finish()
                    clearFindViewByIdCache()
                } else {
                    Toast.makeText(context, getString(R.string.login_fail), Toast.LENGTH_LONG).show()
                }
            }
    }
}

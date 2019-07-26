package com.sa.gym.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import com.sa.gym.viewModel.FirstViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*


class LoginFragment : Fragment() {

    lateinit var providers: List<AuthUI.IdpConfig>

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: FirstViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        showSignInOptions()


        //login screen open
        button_login.setOnClickListener {
            if (edit_email_login.text.toString().isNullOrEmpty())
                edit_email_login.error = "insert email first"
            else if (edit_password_login.text.toString().isNullOrEmpty())
                edit_password_login.error = "insert password first"
            else
                authentication(edit_email_login.text.toString(), edit_password_login.text.toString())
        }

        //forget password
        text_forgetpassword.setOnClickListener {
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, ForgetPasswordFragment()).addToBackStack(null).commit()
            Log.d("main", "text signup clicked")
        }

        //sign-up screen open
        text_signup.setOnClickListener {
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, SignupFragment()).addToBackStack(null).commit()
            Log.d("main", "text signup clicked")
        }
        viewModel = ViewModelProviders.of(this).get(FirstViewModel::class.java)
    }

    private fun showSignInOptions() {

    }

    //login authentication with email and password
    private fun authentication(email: String, password: String) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_LONG).show()
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.container, DashboardFragment())?.addToBackStack(null)?.commit()
                    Log.d("main", "authentication success")
                } else {
                    Toast.makeText(context, "Login failed! Please try again later", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}

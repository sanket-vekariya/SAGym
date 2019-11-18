package com.sa.gym.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import com.sa.gym.utils.FragmentTransaction

class LoginFragment : Fragment() {
    private val email = "email"
    private var publicProfile = "publicProfile"
    private val rcSignIn: Int = 1
    private lateinit var callbackManager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callbackManager = CallbackManager.Factory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        facebook_sign_in_button.setReadPermissions(listOf(publicProfile, email))
        //facebook
        facebook_sign_in_button.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Toast.makeText(context, getString(R.string.cancel), Toast.LENGTH_SHORT).show()
                    return
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(
                        context,
                        getString(R.string.some_error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            })
        //login screen open
        button_login.setOnClickListener {
            when {
                edit_email_login.text.toString().isEmpty() -> edit_email_login.error =
                    getString(R.string.insert_email_first)
                edit_password_login.text.toString().isEmpty() -> edit_password_login.error =
                    getString(R.string.insert_password_first)
                else -> authentication(
                    edit_email_login.text.toString(),
                    edit_password_login.text.toString()
                )
            }
        }
        //forget ic_password
        text_forget_password.setOnClickListener {
            FragmentTransaction().FragTransactionReplacewithBackStack(
                requireFragmentManager(),
                ForgetPasswordFragment(),
                R.id.container
            )
        }
        //sign-up screen open
        text_sign_up.setOnClickListener {
            FragmentTransaction().FragTransactionReplacewithBackStack(
                requireFragmentManager(),
                SignUpFragment(),
                R.id.container
            )
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.addOnCompleteListener {
                    Toast.makeText(context, "welcome " + it.result?.user, Toast.LENGTH_SHORT)
                        .show()
                }
                startActivity(Intent(context, DashboardActivity::class.java))
                activity?.finish()
                clearFindViewByIdCache()
            } else {
                task.exception?.printStackTrace()
                Toast.makeText(context, getString(R.string.login_fail), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(context, "done", Toast.LENGTH_SHORT).show()
        if (requestCode == rcSignIn) {
            Toast.makeText(context, "done", Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //login authentication with email and ic_password
    private fun authentication(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful && FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                    Toast.makeText(context, getString(R.string.login_success), Toast.LENGTH_LONG)
                        .show()
                    startActivity(Intent(context, DashboardActivity::class.java))
                    activity?.finish()
                    clearFindViewByIdCache()
                } else {
                    Toast.makeText(context, getString(R.string.login_fail), Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}
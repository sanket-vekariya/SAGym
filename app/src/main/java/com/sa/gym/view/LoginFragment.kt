package com.sa.gym.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FacebookSdk.sdkInitialize(context)
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

        //forget password
        text_forgetpassword.setOnClickListener {
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, ForgetPasswordFragment()).addToBackStack(null).commit()
        }

        //sign-up screen open
        text_signup.setOnClickListener {
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, SignupFragment()).addToBackStack(null).commit()
        }

        button_login_facebook.setOnClickListener {
            FacebookSdk.sdkInitialize(context)
            facebookAuth()
        }


        //facebook login


    }

    private fun facebookAuth() {
        button_login_facebook.setReadPermissions(listOf("email"))
        button_login_facebook.registerCallback(CallbackManager.Factory.create(),
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

//                    startActivity(Intent(context, DashboardActivity::class.java))
//                    activity?.finish()
                    handleFacebookAccessToken(loginResult.accessToken)
//
//                    Toast.makeText(context, "login result on success", Toast.LENGTH_SHORT).show()
//                    FirebaseAuth.getInstance()
//                        .signInWithCredential(FacebookAuthProvider.getCredential(accessToken.token))
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Toast.makeText(
//                                    context, "Facebook " + getString(R.string.login_success), Toast.LENGTH_LONG
//                                ).show()
//                                startActivity(Intent(context, DashboardActivity::class.java))
//                                activity?.finish()
//                            } else {
//                                Toast.makeText(context, getString(R.string.login_fail), Toast.LENGTH_LONG).show()
//                            }
//                        }
                }

                override fun onCancel() {
                    Toast.makeText(context, "on cancel", Toast.LENGTH_SHORT).show()
                    return
                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(context, "some error occurred", Toast.LENGTH_SHORT).show()
                    return
                }

            })
    }

    private fun handleFacebookAccessToken(token: AccessToken?) {
        val authCredential: AuthCredential = FacebookAuthProvider.getCredential(token!!.token)
        FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(context, "Facebook " + getString(R.string.login_success), Toast.LENGTH_LONG).show()
                startActivity(Intent(context, DashboardActivity::class.java))
                activity?.finish()
                clearFindViewByIdCache()
            } else {
                Toast.makeText(context, "Facebook " + getString(R.string.login_fail), Toast.LENGTH_LONG).show()
            }
        }
    }

    //login authentication with email and password
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        CallbackManager.Factory.create().onActivityResult(requestCode, resultCode, data)
    }
}

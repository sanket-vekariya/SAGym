package com.sa.gym.utils

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import com.sa.gym.view.MainActivity

//Dialog Fragment for SignOut Action
class AlertDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        android.app.AlertDialog.Builder(activity)
            .setMessage(arguments?.getString(ALERT_MESSAGE))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                LoginManager.getInstance().logOut()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(context, MainActivity::class.java))
                activity?.finish()
            }
            .create()

    companion object {
        @JvmStatic
        private val ALERT_MESSAGE = "alert dialog"

        //for accessing object at other places
        @JvmStatic
        fun newInstance(message: String): AlertDialog = AlertDialog().apply {
            arguments = Bundle().apply { putString(ALERT_MESSAGE, message) }
        }
    }
}
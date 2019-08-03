package com.sa.gym

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.view.MainActivity

class AlertDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        android.app.AlertDialog.Builder(activity)
            .setMessage(arguments?.getString(ALERT_MESSAGE))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(context, MainActivity::class.java))
                activity?.finish()
            }
            .create()

    companion object {

        @JvmStatic
        private val ALERT_MESSAGE = "alert dialog"

        @JvmStatic
        fun newInstance(message: String): AlertDialog = AlertDialog().apply {
            arguments = Bundle().apply { putString(ALERT_MESSAGE, message) }
        }
    }
}
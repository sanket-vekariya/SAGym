package com.sa.gym.view


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sa.gym.R
import kotlinx.android.synthetic.main.fragment_update_password.*

class UpdatePasswordFragment : DialogFragment() {

    var mAuthPassword: FirebaseUser? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuthPassword = FirebaseAuth.getInstance().currentUser as FirebaseUser

        //update password button
        button_password_update.setOnClickListener {
            val password = edit_password_update.text.toString().trim()

            if (TextUtils.isEmpty(password)) {
                edit_password_update.error = getString(R.string.insert_password_first)
            } else {
                mAuthPassword!!.updatePassword(password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Password Reset Success!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Fail to Reset Password!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

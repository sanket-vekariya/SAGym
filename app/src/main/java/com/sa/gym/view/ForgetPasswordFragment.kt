package com.sa.gym.view


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import kotlinx.android.synthetic.main.fragment_forget_password.*
import kotlinx.android.synthetic.main.fragment_forget_password.view.*

class ForgetPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_forget_password, container, false)
        view.button_forget_password.setOnClickListener {
            if (edit_email_forget_password.text.toString().isEmpty())
                edit_email_forget_password.error = "insert email first"
            else
                forgetPassword(view.edit_email_forget_password.text.toString())
        }
        return view
    }

    //forgot password method
    private fun forgetPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Check email to reset your password!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.container, LoginFragment())?.commit()
                    Log.d("main", "forget password button success")
                } else {
                    Toast.makeText(context, "Email not exist or Failed to send ", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

}

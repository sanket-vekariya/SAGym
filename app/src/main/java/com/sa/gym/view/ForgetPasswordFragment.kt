package com.sa.gym.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import com.sa.gym.model.FragmentTransaction
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
                edit_email_forget_password.error = getString(R.string.insert_email_first)
            else
                forgetPassword(view.edit_email_forget_password.text.toString())
        }
        return view
    }

    //forgot ic_password method
    private fun forgetPassword(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        getString(R.string.check_your_email_to_reset_your_password),
                        Toast.LENGTH_SHORT
                    ).show()
                    FragmentTransaction().FragTransactionReplacewithoutBackStack(fragmentManager!!,LoginFragment(),R.id.container)

                } else {
                    Toast.makeText(context, getString(R.string.email_not_exist_or_failed_to_send), Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

}

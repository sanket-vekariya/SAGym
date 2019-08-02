package com.sa.gym.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sa.gym.R
import kotlinx.android.synthetic.main.fragment_form_add_user_first.*

class AddUserFirstFormFragment : Fragment() {

    internal var id: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_form_add_user_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragment = AddUserSecondFormFragment()
        val bundle = Bundle()
        val transaction = fragmentManager?.beginTransaction()
        //next button click pass data to next fragment with bundle
        button_next.setOnClickListener {
            if (!edit_firstname.text.isNullOrEmpty() &&
                !edit_lastname.text.isNullOrEmpty() &&
                !edit_active.text.isNullOrEmpty() &&
                !edit_email.text.isNullOrEmpty() &&
                !edit_contact.text.isNullOrEmpty() &&
                !edit_dob.text.isNullOrEmpty() &&
                !edit_address.text.isNullOrEmpty() &&
                !edit_intime.text.isNullOrEmpty() &&
                !edit_outtime.text.isNullOrEmpty()
            ) {
                bundle.putString("id", id)
                bundle.putString("firstName", edit_firstname.text.toString())
                bundle.putString("lastName", edit_lastname.text.toString())
                bundle.putBoolean("active", edit_active.text.toString().toBoolean())
                bundle.putString("email", edit_email.text.toString())
                bundle.putLong("contact", edit_contact.text.toString().toLong())
                bundle.putString("intime", edit_intime.text.toString())
                bundle.putString("outtime", edit_outtime.text.toString())
                bundle.putString("dob", edit_dob.text.toString())
                bundle.putString("address", edit_address.text.toString())

                fragment.arguments = bundle
                transaction!!.replace(R.id.container_activity_add_user, fragment).addToBackStack(null).commit()
            } else {
                Toast.makeText(context, "Insert All The Data First", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

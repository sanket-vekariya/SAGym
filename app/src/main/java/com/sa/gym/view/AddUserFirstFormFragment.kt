package com.sa.gym.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sa.gym.R
import com.sa.gym.utils.DatePicker
import com.sa.gym.utils.TimePicker
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

        edit_dob.setOnClickListener {
            val a = DatePicker()
            a.setTargetFragment(this, 123)
            a.show(requireFragmentManager(), "Date Picker")
        }
        edit_in_time.setOnClickListener {
            val b = TimePicker()
            b.setTargetFragment(this, 111)
            b.show(requireFragmentManager(), "Time Picker")
        }
        edit_out_time.setOnClickListener {
            val b = TimePicker()
            b.setTargetFragment(this, 222)
            b.show(requireFragmentManager(), "Time Picker")
        }
        //next button click pass data to next fragment with bundle
        button_next.setOnClickListener {
            if (!edit_first_name.text.isNullOrEmpty() &&
                !edit_last_name.text.isNullOrEmpty() &&
                !edit_email.text.isNullOrEmpty() &&
                !edit_contact.text.isNullOrEmpty() &&
                !edit_dob.text.isNullOrEmpty() &&
                !edit_address.text.isNullOrEmpty() &&
                !edit_in_time.text.isNullOrEmpty() &&
                !edit_out_time.text.isNullOrEmpty()
            ) {
                bundle.putString("id", id)
                bundle.putString("firstName", edit_first_name.text.toString())
                bundle.putString("lastName", edit_last_name.text.toString())
                bundle.putString("email", edit_email.text.toString())
                bundle.putLong("contact", edit_contact.text.toString().toLong())
                bundle.putString("intime", edit_in_time.text.toString())
                bundle.putString("outtime", edit_out_time.text.toString())
                bundle.putString("dob", edit_dob.text.toString())
                bundle.putString("address", edit_address.text.toString())

                fragment.arguments = bundle
                transaction!!.replace(R.id.container_activity_add_user, fragment).addToBackStack(null).commit()
            } else {
                Toast.makeText(context, getString(R.string.insert_all_the_data_first), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123 && data != null) {
                edit_dob.setText(data.getStringExtra("key_date"))
            } else if (requestCode == 111 && data != null) {
                edit_in_time.setText(data.getStringExtra("key_time"))
            } else if (requestCode == 222 && data != null) {
                edit_out_time.setText(data.getStringExtra("key_time"))
            }
        }
    }
}

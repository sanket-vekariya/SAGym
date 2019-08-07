package com.sa.gym.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.sa.gym.R
import java.util.*


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    //on date set time picker dialog open with passing value to it with bundle
    @SuppressLint("CommitPrefEdits")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val editor: SharedPreferences.Editor =
            activity?.applicationContext!!.getSharedPreferences("date", MODE_PRIVATE).edit() as SharedPreferences.Editor
        editor.putInt("dayOfMonth", dayOfMonth)
        editor.putInt("month", month)
        editor.putInt("year", year)
        editor.apply()
        val frag = TimePickerFragment()
        frag.show(requireFragmentManager(), getString(R.string.time_picker_dialog_box))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val date = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DATE, date)

        val custom = DatePickerDialog(context!!, this, year, month, date)
        custom.datePicker.minDate = calendar.timeInMillis - (1000 * 60 * 60 * 24 * 7)

        return custom
    }
}
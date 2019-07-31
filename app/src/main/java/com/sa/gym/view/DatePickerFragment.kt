package com.sa.gym.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var year: Int = 0
    var date: Int = 0
    var month: Int = 0

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val frag = TimePickerFragment()
        frag.show(requireFragmentManager().beginTransaction(), "Time Picker Dialog Box")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        date = calendar.get(Calendar.DATE)
        month = calendar.get(Calendar.MONTH)

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DATE, date)


        val custom = DatePickerDialog(context!!, this, year, month, date)

        custom.datePicker.maxDate = calendar.timeInMillis + (1000 * 60 * 60 * 24 * 7)
        custom.datePicker.minDate = calendar.timeInMillis - (1000 * 60 * 60 * 24 * 7)

        return custom
    }

    @SuppressLint("CommitPrefEdits")
    override fun onDestroyView() {
        super.onDestroyView()
        val editor: SharedPreferences.Editor =
            activity?.applicationContext!!.getSharedPreferences("date", MODE_PRIVATE).edit() as SharedPreferences.Editor
        editor.putInt("dayOfMonth", date)
        editor.putInt("month", month)
        editor.putInt("year", year)
        editor.commit()
    }
}
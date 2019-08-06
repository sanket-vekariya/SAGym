package com.sa.gym.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePicker : DialogFragment(), DatePickerDialog.OnDateSetListener {

    //on date set time picker dialog open with passing value to it with bundle
    @SuppressLint("CommitPrefEdits")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val intent = Intent()
        intent.putExtra("key_date", "$dayOfMonth/$month/$year")
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
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
        custom.datePicker.maxDate = calendar.timeInMillis - (1000 * 60 * 60 * 24 * 7)

        return custom
    }
}
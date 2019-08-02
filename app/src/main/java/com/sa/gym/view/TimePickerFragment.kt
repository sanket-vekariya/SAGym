package com.sa.gym.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.sa.gym.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var hourOfDay: Int = 0
    private var minute: Int = 0
    private var second: Int = 0
    private var dayOfMonth: Int = 0
    private var month: Int = 0
    private var year: Int = 0

    private val calendar = Calendar.getInstance()

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        setReminderInCalendar("$year,${month + 1},$dayOfMonth,$hourOfDay,$minute", "Reminder", "Gym Reminder")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val prefs: SharedPreferences = activity?.applicationContext!!.getSharedPreferences("date", MODE_PRIVATE)
        dayOfMonth = prefs.getInt("dayOfMonth", 0)
        month = prefs.getInt("month", 0)
        year = prefs.getInt("year", 0)

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DATE, dayOfMonth)

        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)

        return TimePickerDialog(activity, theme, this, hourOfDay, minute, true)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setReminderInCalendar(startDate: String, title: String, description: String) {
        var stDate = startDate

        val calDate = GregorianCalendar()

        val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val targetFormat = SimpleDateFormat("yyyy,MM,dd,HH,mm")
        val date: Date
        try {
            date = originalFormat.parse(startDate)
            stDate = targetFormat.format(date)

        } catch (ex: ParseException) {
        }

        val dates = stDate.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val startYear = dates[0]
        val startMonth = dates[1]
        val startDay = dates[2]
        val startHour = dates[3]
        val startMinute = dates[4]

        calDate.set(
            Integer.parseInt(startYear),
            Integer.parseInt(startMonth) - 1,
            Integer.parseInt(startDay),
            Integer.parseInt(startHour),
            Integer.parseInt(startMinute)
        )
        val startMillis: Long = calDate.timeInMillis

        try {
            val cr = activity?.contentResolver
            val values = ContentValues()
            values.put(CalendarContract.Events.DTSTART, startMillis)
            values.put(CalendarContract.Events.DTEND, calDate.timeInMillis + 1000)
            values.put(CalendarContract.Events.TITLE, title)
            values.put(CalendarContract.Events.DESCRIPTION, description)
            values.put(CalendarContract.Events.HAS_ALARM, 1)
            values.put(CalendarContract.Events.CALENDAR_ID, 1)
            values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().timeZone.id)
            if (ActivityCompat.checkSelfPermission(
                    activity?.applicationContext!!,
                    Manifest.permission.WRITE_CALENDAR
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR),
                    0
                )
                return
            }
            cr?.insert(CalendarContract.Events.CONTENT_URI, values)
            Toast.makeText(context, getString(R.string.reminder_set_successfully), Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(context, getString(R.string.failed_to_set_reminder), Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }
}
package com.sa.gym.view

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.sa.gym.adapter.TaskManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.*
import kotlin.coroutines.CoroutineContext


class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    var hourOfDay: Int = 0
    var minute: Int = 0
    var second: Int = 0
    val calendar = Calendar.getInstance()

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val prefs: SharedPreferences = activity?.applicationContext!!.getSharedPreferences("date", MODE_PRIVATE)
        val dayOfMonth = prefs.getInt("dayOfMonth", 0)
        val month = prefs.getInt("month", 0)
        val year = prefs.getInt("year", 0)
        calendar.set(Calendar.YEAR, dayOfMonth)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, year)

        setAlarm(calendar)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)

        val custom = TimePickerDialog(activity, theme, this, hourOfDay, minute, true)
        return custom
    }

    fun setAlarm(calendar: Calendar) {
        val alarmManager: AlarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent: Intent = Intent(context, TaskManager::class.java)
        intent.action = "com.sa.gym.view.ReminderFragment"
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.set(AlarmManager.RTC, calendar.timeInMillis, pendingIntent)
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 1000, pendingIntent)
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm() {
        val alarmManager: AlarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: Intent = Intent(context, TaskManager::class.java)
        intent.action = "com.sa.gym.view.ReminderFragment"
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }
}
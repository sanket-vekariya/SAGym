package com.sa.gym.adapter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.facebook.FacebookSdk.getApplicationContext
import android.media.RingtoneManager


class TaskManager : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("com.sa.gym.view.ReminderFragment")) {
            try {
                val alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                val ring = RingtoneManager.getRingtone(context, alarm)
                ring.play()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
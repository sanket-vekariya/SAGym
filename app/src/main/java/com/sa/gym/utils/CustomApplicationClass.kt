package com.sa.gym.utils

import android.app.Application

//For changing entire app's Font to "ROBOTO"
class CustomApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        TypefaceUtil.overrideFont(this, "SERIF", "fonts/roboto.ttf")
    }
}
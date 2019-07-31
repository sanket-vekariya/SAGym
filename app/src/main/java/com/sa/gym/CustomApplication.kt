package com.sa.gym

import android.app.Application

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TypefaceUtil.overrideFont(this, "SERIF", "fonts/roboto.ttf")
    }
}
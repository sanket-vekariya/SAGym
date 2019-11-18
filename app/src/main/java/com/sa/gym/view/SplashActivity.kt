package com.sa.gym.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import kotlinx.coroutines.Runnable
import kotlin.system.exitProcess

class SplashActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 3000
    private var mInternetConnection: Boolean = false
    private val currentFireBaseUser = FirebaseAuth.getInstance().currentUser
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private val mRunnable: Runnable = Runnable {
        if (currentFireBaseUser != null || AccessToken.getCurrentAccessToken() != null) {
            startActivity(Intent(applicationContext, DashboardActivity::class.java))
            finish()
        }
        if (currentFireBaseUser == null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Internet Availability
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        mInternetConnection = activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun internetDialogBox() {
        val alert: AlertDialog
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        mInternetConnection = activeNetworkInfo != null && activeNetworkInfo.isConnected

        if (!mInternetConnection) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.need_internet))
                .setTitle(getString(R.string.unable_to_connect))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.settings)) { _, _ ->
                    startActivityForResult(Intent(Settings.ACTION_SETTINGS), 0)
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    exitProcess(0)
                }
                .setRecycleOnMeasureEnabled(true)

            alert = builder.create()
            alert.show()
        } else {
            mDelayHandler = Handler()
            mDelayHandler!!.postDelayed(mRunnable, splashDelay)
        }
    }

    override fun onStart() {
        super.onStart()
        if (!mInternetConnection) {
            internetDialogBox()
        } else {
            mDelayHandler = Handler()
            mDelayHandler!!.postDelayed(mRunnable, splashDelay)
        }
    }
}
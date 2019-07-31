package com.sa.gym.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.util.*


class DashboardActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container_dashboard, DashboardFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_list_user -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container_dashboard, UserListFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        toolbar.title = FirebaseAuth.getInstance().currentUser?.email.toString()
        setSupportActionBar(toolbar)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home

        fab_add_user.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_signout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return true
            }
            R.id.menu_map -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container_dashboard, MapFragment()).commit()
                return true
            }
            R.id.menu_set_reminder -> {
                DatePickerFragment().show(supportFragmentManager, "Date Picker Dialog Box")
                return true
            }
            R.id.menu_cancel_reminder -> {
//                val calendar : Calendar = Calendar.getInstance()
//                TimePickerFragment().cancelAlarm(calendar)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

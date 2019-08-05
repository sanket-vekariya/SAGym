package com.sa.gym.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import com.sa.gym.view.utils.AlertDialog
import kotlinx.android.synthetic.main.activity_dashboard.*


class DashboardActivity : AppCompatActivity() {

    //Bottom navigation items selection
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_dashboard -> {
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
        navigation.selectedItemId = R.id.navigation_dashboard


        fab_add_user.setOnClickListener {
            startActivity(Intent(this, AddUserActivity::class.java))
        }
    }

    //more options menu create
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //more options selection navigation
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_set_reminder -> {
                DatePickerFragment().show(supportFragmentManager, getString(R.string.date_picker_dialog_box))
                return true
            }
            R.id.menu_map -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container_dashboard, MapFragment()).commit()
                return true
            }
            R.id.menu_update_password -> {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container_dashboard, UpdatePasswordFragment()).commit()
                return true
            }
            R.id.menu_sign_out -> {
                AlertDialog.newInstance(getString(R.string.really_want_to_signout))
                    .show(supportFragmentManager, "alert dialog")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

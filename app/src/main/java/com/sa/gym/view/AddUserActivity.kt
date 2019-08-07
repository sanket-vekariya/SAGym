package com.sa.gym.view

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.sa.gym.R
import com.sa.gym.model.FragmentTransaction

class AddUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        FragmentTransaction().FragTransactionAddwithoutBackStack(supportFragmentManager,AddUserFirstFormFragment(),R.id.container_activity_add_user)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}

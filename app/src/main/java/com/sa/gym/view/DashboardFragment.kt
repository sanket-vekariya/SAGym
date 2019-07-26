package com.sa.gym.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import com.sa.gym.viewModel.SecondViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardFragment()
    }

    private lateinit var viewModel: SecondViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prefs = context?.getSharedPreferences("SharedPreference", Context.MODE_PRIVATE)
        toolbar.title = FirebaseAuth.getInstance().currentUser?.email.toString()
        button_signout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, LoginFragment()).addToBackStack(null).commit()
            Log.d("main", "sign-out successfully")
        }
        viewModel = ViewModelProviders.of(this).get(SecondViewModel::class.java)
    }
}
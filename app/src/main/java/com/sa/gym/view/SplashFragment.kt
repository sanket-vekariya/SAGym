package com.sa.gym.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.sa.gym.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class SplashFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch {
            delay(2000)
            withContext(Dispatchers.Main) {

                //redirect to dashboard as like session with normal email and password
                if (FirebaseAuth.getInstance().currentUser != null && FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                    startActivity(Intent(context, DashboardActivity::class.java))
                    activity?.finish()
                }
                if (FirebaseAuth.getInstance().currentUser == null) {
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.container, LoginFragment())?.commit()
                    activity?.finish()
                    Toast.makeText(context, "either signout or email not varified yet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
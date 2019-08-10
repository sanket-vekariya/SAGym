package com.sa.gym.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentTransaction {

    fun FragTransactionReplacewithBackStack(fragmentManager: FragmentManager, to: Fragment, container: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(container, to).addToBackStack(null).commit()
    }

    fun FragTransactionReplacewithoutBackStack(fragmentManager: FragmentManager, to: Fragment, container: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(container, to).commit()
    }

    fun FragTransactionAddwithoutBackStack(fragmentManager: FragmentManager, to: Fragment, container: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(container, to).commit()
    }

}

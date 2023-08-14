package com.example.cookbook.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentUtils {
    @Suppress("UNCHECKED_CAST")
    fun <T: Fragment> obtainFragment(
        fragmentManager: FragmentManager,
        fragmentClass: Class<T>,
        newInstance: () -> T
    ): T {
        val existingFragment = fragmentManager.findFragmentByTag(fragmentClass::class.java.simpleName)
        return existingFragment as? T ?: newInstance()
    }
}
package com.example.cookbook.utils.navigation

interface OnFragmentSwitchListener {
    fun onFragmentSwitched(tag: String?)
    fun pushFragmentToStack(tag: String)
    fun popFragmentFromStack(): String?
}
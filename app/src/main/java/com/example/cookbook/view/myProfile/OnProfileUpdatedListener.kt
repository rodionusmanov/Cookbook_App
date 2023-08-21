package com.example.cookbook.view.myProfile

interface OnProfileUpdatedListener {
    fun onProfileUpdated(
        name: String,
        secondName: String
    )
}
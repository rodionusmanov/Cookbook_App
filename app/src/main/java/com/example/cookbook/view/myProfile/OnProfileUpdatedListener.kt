package com.example.cookbook.view.myProfile

import android.net.Uri

interface OnProfileUpdatedListener {
    fun onProfileUpdated(
        name: String,
        secondName: String,
        avatarUri: Uri?
    )
}
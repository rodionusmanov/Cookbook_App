package com.example.cookbook.model.repository.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.cookbook.utils.SELECTED_DIET_KEY
import com.example.cookbook.utils.SELECTED_INTOLERANCES_KEY
import com.example.cookbook.utils.SHARED_PREFERENCES_DIETARY_NAME

class DietaryRestrictionsRepository(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_DIETARY_NAME, Context.MODE_PRIVATE)
    
    fun getSelectedDiets(): String {
        return getStringFromPreferences(SELECTED_DIET_KEY)
    }

    fun getSelectedIntolerances(): String {
        return getStringFromPreferences(SELECTED_INTOLERANCES_KEY)
    }

    private fun getStringFromPreferences(selectedDietKey: String): String {
        val set = sharedPreferences.getStringSet(selectedDietKey, emptySet())
        return set?.joinToString(separator = ",") ?: ""
    }
}
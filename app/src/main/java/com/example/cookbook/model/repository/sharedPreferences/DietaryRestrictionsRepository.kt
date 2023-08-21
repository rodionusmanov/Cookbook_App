package com.example.cookbook.model.repository.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.cookbook.utils.PROFILE_NAME_KEY
import com.example.cookbook.utils.PROFILE_SECOND_NAME_KEY
import com.example.cookbook.utils.SELECTED_DIET_KEY
import com.example.cookbook.utils.SELECTED_INTOLERANCES_KEY
import com.example.cookbook.utils.SHARED_PREFERENCES_DIETARY_NAME

class DietaryRestrictionsRepository(context: Context) {

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

    fun saveSelectedRestrictions(restrictions: Set<String>, preferenceKey: String) {
        sharedPreferences.edit().putStringSet(preferenceKey, restrictions).apply()
    }

    fun getSelectedRestrictions(preferenceKey: String): MutableSet<String>{
        return sharedPreferences.getStringSet(preferenceKey, mutableSetOf()) ?: mutableSetOf()
    }

    fun saveProfileName(name:String) {
        sharedPreferences.edit().putString(PROFILE_NAME_KEY, name).apply()
    }

    fun saveProfileSecondName(secondName: String) {
        sharedPreferences.edit().putString(PROFILE_SECOND_NAME_KEY, secondName).apply()
    }

    fun getProfileName(): String {
        return sharedPreferences.getString(PROFILE_NAME_KEY, "") ?: ""
    }

    fun getProfileSecondName(): String {
        return sharedPreferences.getString(PROFILE_SECOND_NAME_KEY, "") ?: ""
    }
}
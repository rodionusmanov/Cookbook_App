package com.example.cookbook.view.myProfile

import androidx.lifecycle.ViewModel
import com.example.cookbook.model.repository.sharedPreferences.SharedPreferencesRepository

class MyProfileViewModel(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : ViewModel() {

    fun getSelectedRestrictions(preferenceKey: String): MutableSet<String> {
        return sharedPreferencesRepository.getSelectedRestrictions(preferenceKey)
    }

    fun saveSelectedRestrictions(restrictions: Set<String>, preferenceKey: String) {
        sharedPreferencesRepository.saveSelectedRestrictions(restrictions, preferenceKey)
    }

    fun saveProfile(name: String, secondName: String) {
        sharedPreferencesRepository.apply {
            saveProfileName(name)
            saveProfileSecondName(secondName)
        }
    }

    fun getProfileName(): String {
        return sharedPreferencesRepository.getProfileName()
    }

    fun getProfileSecondName(): String {
        return sharedPreferencesRepository.getProfileSecondName()
    }
}
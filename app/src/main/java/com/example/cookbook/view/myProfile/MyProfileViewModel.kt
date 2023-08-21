package com.example.cookbook.view.myProfile

import androidx.lifecycle.ViewModel
import com.example.cookbook.model.repository.sharedPreferences.DietaryRestrictionsRepository

class MyProfileViewModel(
    private val dietaryRestrictionsRepository: DietaryRestrictionsRepository
) : ViewModel() {

    fun getSelectedRestrictions(preferenceKey: String): MutableSet<String> {
        return dietaryRestrictionsRepository.getSelectedRestrictions(preferenceKey)
    }

    fun saveSelectedRestrictions(restrictions: Set<String>, preferenceKey: String) {
        dietaryRestrictionsRepository.saveSelectedRestrictions(restrictions, preferenceKey)
    }

}
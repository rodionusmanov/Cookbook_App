package com.example.cookbook.view.myProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.FragmentMyProfileBinding
import com.example.cookbook.utils.SELECTED_DIET_KEY
import com.example.cookbook.utils.SELECTED_INTOLERANCES_KEY
import com.example.cookbook.utils.SHARED_PREFERENCES_DIETARY_NAME
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    private var isDietBlockOpen = false
    private var isIntoleranceBlockOpen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        initTextViewBlockListener()
        initChipGroups()
        return binding.root
    }

    private fun initChipGroups() {
        initChipGroup(binding.dietsChipGroup, SELECTED_DIET_KEY)
        initChipGroup(binding.intolerancesChipGroup, SELECTED_INTOLERANCES_KEY)
    }

    private fun initChipGroup(chipGroup: ChipGroup, preferenceKey: String) {
        val selectedItems = getSelectedRestrictions(preferenceKey)
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (selectedItems.contains(chip.text.toString())) {
                chip.isChecked = true
            }
        }

        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val newSelectedItems = mutableSetOf<String>()
            for (id in checkedIds) {
                val chip = group.findViewById<Chip>(id)
                chip?.let {
                    newSelectedItems.add(it.text.toString())
                }
            }
            saveSelectedRestrictions(newSelectedItems, preferenceKey)
        }
    }

    private fun saveSelectedRestrictions(restrictions: Set<String>, preferenceKey: String) {
        val sharedPreferences = activity?.getSharedPreferences(
            SHARED_PREFERENCES_DIETARY_NAME, AppCompatActivity.MODE_PRIVATE
        )
        sharedPreferences?.edit()?.putStringSet(preferenceKey, restrictions)?.apply()
    }

    private fun getSelectedRestrictions(preferenceKey: String): MutableSet<String> {
        val sharedPreferences = activity?.getSharedPreferences(
            SHARED_PREFERENCES_DIETARY_NAME, AppCompatActivity.MODE_PRIVATE
        )
        return sharedPreferences?.getStringSet(preferenceKey, mutableSetOf()) ?: mutableSetOf()
    }

    private fun initTextViewBlockListener() {
        with(binding) {
            dietsText.setOnClickListener {
                if(isDietBlockOpen) {
                    dietsChipGroup.visibility = View.GONE
                    animateBlockCloseMark(binding.dietBlockMark)
                    isDietBlockOpen = false
                } else {
                    dietsChipGroup.visibility = View.VISIBLE
                    animateBlockOpenMark(binding.dietBlockMark)
                    isDietBlockOpen = true
                }
            }

            intolerancesText.setOnClickListener {
                if(isIntoleranceBlockOpen) {
                    intolerancesChipGroup.visibility = View.GONE
                    animateBlockCloseMark(binding.intoleranceBlockMark)
                    isIntoleranceBlockOpen = false
                } else {
                    intolerancesChipGroup.visibility = View.VISIBLE
                    animateBlockOpenMark(binding.intoleranceBlockMark)
                    isIntoleranceBlockOpen = true
                }
            }
        }
    }

    private fun animateBlockCloseMark(cardView: MaterialCardView) {
        val rotate = RotateAnimation(
            180f, 0f,
            Animation.RELATIVE_TO_SELF,
            0.5F,
            Animation.RELATIVE_TO_SELF,
            0.5F
        )
        rotate.duration = 300
        rotate.fillAfter = true
        cardView.startAnimation(rotate)
    }

    private fun animateBlockOpenMark(cardView: MaterialCardView) {
        val rotate = RotateAnimation(
            0f, 180f,
            Animation.RELATIVE_TO_SELF,
            0.5F,
            Animation.RELATIVE_TO_SELF,
            0.5F
        )
        rotate.duration = 300
        rotate.fillAfter = true
        cardView.startAnimation(rotate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.cookbook.view.myProfile

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import coil.load
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentMyProfileBinding
import com.example.cookbook.utils.SELECTED_DIET_KEY
import com.example.cookbook.utils.SELECTED_INTOLERANCES_KEY
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    private var isDietBlockOpen = false
    private var isIntoleranceBlockOpen = false

    private val model: MyProfileViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        initTextViewBlockListener()
        initUserNameTextView()
        initChipGroups()
        initEditButton()
        loadAvatar()
        return binding.root
    }

    private fun loadAvatar() {
        val file = File(requireContext().filesDir, "avatar_image.jpg")
        if (file.exists()) {
            binding.userAvatarImage.load(file)
        }
    }

    private fun initUserNameTextView() {
        with(binding) {
            userName.text = model.getProfileName().ifBlank {
                getString(R.string.profile_empty_name_cell)
            }

            userSecondName.text = model.getProfileSecondName().ifBlank {
                getString(R.string.profile_empty_second_name_cell)
            }
        }
    }

    private fun initEditButton() {
        binding.editProfileButton.setOnClickListener {
            childFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_from_bottom, 0,0, R.anim.slide_out_to_bottom
                )
                .replace(R.id.my_profile_container, EditProfileFragment.newInstance())
                .addToBackStack(null)
                .commit()
            binding.myProfileContainer.visibility = View.VISIBLE
        }
    }

    private fun initChipGroups() {
        initChipGroup(
            binding.dietsChipGroup,
            SELECTED_DIET_KEY
        )
        initChipGroup(
            binding.intolerancesChipGroup,
            SELECTED_INTOLERANCES_KEY
        )
    }

    private fun initChipGroup(
        chipGroup: ChipGroup,
        preferenceKey: String,
    ) {
        val selectedItems = model.getSelectedRestrictions(preferenceKey)

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip

            chip.isVisible = selectedItems.contains(chip.text.toString())
            chip.isChecked = chip.isVisible

            chip.setOnCheckedChangeListener { _, isChecked ->
                if(!isDietBlockOpen || !isIntoleranceBlockOpen) {
                    TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
                }

                val newSelectedItems = getSelectedChipsText(chipGroup)
                model.saveSelectedRestrictions(newSelectedItems, preferenceKey)
            }
        }
    }

    private fun getSelectedChipsText(chipGroup: ChipGroup): Set<String> {
        val selectedItems = mutableSetOf<String>()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedItems.add(chip.text.toString())
            }
        }
        return selectedItems
    }

    private fun initTextViewBlockListener() {
        with(binding) {
            dietsText.setOnClickListener {
                isDietBlockOpen = !isDietBlockOpen
                if(isDietBlockOpen) {
                    animateBlockOpenMark(binding.dietBlockMark)
                } else {
                    animateBlockCloseMark(binding.dietBlockMark)
                }
                handleChipVisibilityAnimation(dietsChipGroup, isDietBlockOpen)
            }

            intolerancesText.setOnClickListener {
                isIntoleranceBlockOpen = !isIntoleranceBlockOpen
                if(isIntoleranceBlockOpen) {
                    animateBlockOpenMark(binding.intoleranceBlockMark)
                } else {
                    animateBlockCloseMark(binding.intoleranceBlockMark)
                }
                handleChipVisibilityAnimation(intolerancesChipGroup, isIntoleranceBlockOpen)
            }
        }
    }

    private fun handleChipVisibilityAnimation(targetGroup: ChipGroup, isOpen: Boolean) {
        val transition = AutoTransition()
        TransitionManager.beginDelayedTransition(binding.root, transition)

        if (isOpen) {
            targetGroup.forEach { it.isVisible = true }
        } else {
            val isChipChecked = {chip: View -> (chip as Chip).isChecked}
            targetGroup.forEach {
                it.isVisible = isChipChecked(it)
            }
        }
    }

    private fun animateBlockCloseMark(cardView: MaterialCardView) {
        val rotate = RotateAnimation(
            180f, 0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
           0.5f
       )
       rotate.duration = 300
       rotate.fillAfter = true
        cardView.startAnimation(rotate)
    }

    private fun animateBlockOpenMark(cardView: MaterialCardView) {
        val rotate = RotateAnimation(
            0f, 180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 300
        rotate.fillAfter = true
        cardView.startAnimation(rotate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onProfileUpdated(name: String, secondName: String, avatarUri: Uri?) {
        with(binding) {
            userName.text = name.ifBlank {
                getString(R.string.profile_empty_name_cell)
            }
            userSecondName.text = secondName.ifBlank {
                getString(R.string.profile_empty_second_name_cell)
            }

            Handler(Looper.getMainLooper()).postDelayed({
                myProfileContainer.visibility = View.GONE
            }, 500)

            avatarUri?.let { binding.userAvatarImage.load(it) }
        }
    }
}
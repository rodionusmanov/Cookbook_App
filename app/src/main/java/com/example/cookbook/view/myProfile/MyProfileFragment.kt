package com.example.cookbook.view.myProfile

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.FragmentMyProfileBinding
import coil.load
import com.example.cookbook.R
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
                .replace(R.id.my_profile_container, EditProfileFragment.newInstance())
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
        var isProgrammaticallyChecked = false

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            chip.setOnCheckedChangeListener { _, isChecked ->
                if(isProgrammaticallyChecked) return@setOnCheckedChangeListener
                if(isChecked) {
                    val newSelectedItems = getSelectedChipsText(chipGroup)
                    model.saveSelectedRestrictions(newSelectedItems, preferenceKey)
                }
            }
            if (selectedItems.contains(chip.text.toString())) {
                isProgrammaticallyChecked = true
                chip.isChecked = true
                isProgrammaticallyChecked = false
            }
        }
    }

    private fun getSelectedChipsText(chipGroup: ChipGroup): Set<String> {
        val selectedItems = mutableSetOf<String>()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if(chip.isChecked) {
                selectedItems.add(chip.text.toString())
            }
        }
        return selectedItems
    }

    private fun initTextViewBlockListener() {
        with(binding) {
            dietsText.setOnClickListener {
                isDietBlockOpen = if (isDietBlockOpen) {
                    closeChipGroup(dietsChipGroup, dietBlock)
                    animateBlockCloseMark(dietBlockMark)
                    false
                } else {
                    openChipGroup(dietsChipGroup, dietBlock)
                    animateBlockOpenMark(dietBlockMark)
                    true
                }
            }

            intolerancesText.setOnClickListener {
                isIntoleranceBlockOpen = if (isIntoleranceBlockOpen) {
                    closeChipGroup(intolerancesChipGroup, intolerancesBlock)
                    animateBlockCloseMark(intoleranceBlockMark)
                    false
                } else {
                    openChipGroup(intolerancesChipGroup, intolerancesBlock)
                    animateBlockOpenMark(intoleranceBlockMark)
                    true
                }
            }
        }
    }

    private fun openChipGroup(chipGroup: ChipGroup, parentLayout: LinearLayout) {

        val height = getMaxHeightForChips(chipGroup)
        val parentOriginalHeight = parentLayout.measuredHeight

        val chipSlideAnimator = ObjectAnimator
            .ofFloat(chipGroup, "translationY", -height.toFloat(), 0f)

        val chipAlphaAnimator = ObjectAnimator
            .ofFloat(chipGroup, "alpha", 0f, 1f)
            .setDuration(150)

        val chipLayoutAnimator = ValueAnimator.ofInt(0, height)
        chipLayoutAnimator.addUpdateListener { animation ->
            val layoutParams = chipGroup.layoutParams
            layoutParams.height = animation.animatedValue as Int
            chipGroup.requestLayout()
        }

        val parentLayoutAnimator = ValueAnimator
            .ofInt(parentOriginalHeight, parentOriginalHeight + height)
        parentLayoutAnimator.addUpdateListener { animation ->
            val layoutParams = parentLayout.layoutParams
            layoutParams.height = animation.animatedValue as Int
            parentLayout.requestLayout()
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(chipSlideAnimator, chipLayoutAnimator, parentLayoutAnimator)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                chipGroup.visibility = View.VISIBLE
            }
        })
        animatorSet.duration = 300
        chipAlphaAnimator.start()
        animatorSet.start()
    }

    private fun getMaxHeightForChips(chipGroup: ChipGroup): Int {
        if (chipGroup.childCount == 0) {
            return 0
        }
        val chip = chipGroup.getChildAt(0) as Chip
        chip.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        return chip.measuredHeight * 5
    }

    private fun closeChipGroup(chipGroup: ChipGroup, parentLayout: LinearLayout) {

        val height = chipGroup.measuredHeight
        val parentHeight = parentLayout.measuredHeight

        val chipSlideAnimator = ObjectAnimator
            .ofFloat(chipGroup, "translationY", 0f, -height.toFloat())

        val chipAlphaAnimator = ObjectAnimator
            .ofFloat(chipGroup, "alpha", 1f, 0f)
            .setDuration(150)

        val parentLayoutAnimator = ValueAnimator.ofInt(parentHeight, parentHeight - height)
        parentLayoutAnimator.addUpdateListener { animation ->
            val parentLayoutParams = parentLayout.layoutParams
            parentLayoutParams.height = animation.animatedValue as Int
            parentLayout.layoutParams = parentLayoutParams
        }

        AnimatorSet().also { set ->
            with(set) {
                playTogether(chipSlideAnimator, parentLayoutAnimator)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        chipGroup.visibility = View.INVISIBLE
                        chipGroup.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
                })
                duration = 300
            }
        }.start()
        chipAlphaAnimator.start()
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
            myProfileContainer.visibility = View.GONE
            avatarUri?.let { binding.userAvatarImage.load(it) }
        }
    }
}
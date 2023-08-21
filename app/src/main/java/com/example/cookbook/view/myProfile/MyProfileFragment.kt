package com.example.cookbook.view.myProfile

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.LinearLayout
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
                isDietBlockOpen = if(isDietBlockOpen) {
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
                isIntoleranceBlockOpen = if(isIntoleranceBlockOpen) {
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

    private fun openChipGroup(chipGroup: ChipGroup, parentLayout: LinearLayout){
        chipGroup.measure(
            View.MeasureSpec.makeMeasureSpec(parentLayout.width, View.MeasureSpec.AT_MOST),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val height = chipGroup.measuredHeight
        chipGroup.layoutParams.height = 0

        val chipSlideAnimator = ObjectAnimator
            .ofFloat(chipGroup, "translationY", -height.toFloat(), 0f)

        val chipAlphaAnimator = ObjectAnimator
            .ofFloat(chipGroup, "alpha", 0f, 1f)
            .setDuration(150)

        val parentLayoutAnimator = ValueAnimator.ofInt(0, height)
        parentLayoutAnimator.addUpdateListener { animation ->
            val layoutParams = chipGroup.layoutParams
            layoutParams.height = animation.animatedValue as Int
            chipGroup.requestLayout()
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(chipSlideAnimator, parentLayoutAnimator)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                chipGroup.visibility = View.VISIBLE
            }
        })
        animatorSet.duration = 300
        chipAlphaAnimator.start()
        animatorSet.start()
    }

    private fun closeChipGroup(chipGroup: ChipGroup, parentLayout: LinearLayout){

        chipGroup.measure(
            View.MeasureSpec.makeMeasureSpec(parentLayout.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )

        val height = chipGroup.measuredHeight

        val chipSlideAnimator = ObjectAnimator
            .ofFloat(chipGroup, "translationY", 0f, -height.toFloat())

        val chipAlphaAnimator = ObjectAnimator
            .ofFloat(chipGroup, "alpha", 1f, 0f)
            .setDuration(150)

        val layoutParams = chipGroup.layoutParams
        val parentLayoutAnimator = ValueAnimator.ofInt(height, 0)
        parentLayoutAnimator.addUpdateListener { animation ->
            layoutParams.height = animation.animatedValue as Int
            chipGroup.layoutParams = layoutParams
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(chipSlideAnimator, parentLayoutAnimator)
        animatorSet.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                chipGroup.visibility = View.GONE
            }
        })
        animatorSet.duration = 300
        chipAlphaAnimator.start()
        animatorSet.start()
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
}
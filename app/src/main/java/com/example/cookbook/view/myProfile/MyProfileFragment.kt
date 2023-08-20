package com.example.cookbook.view.myProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.FragmentMyProfileBinding
import com.google.android.material.card.MaterialCardView

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        initTextViewBlockListener()
        return binding.root
    }

    private fun initTextViewBlockListener() {
        with(binding){
            dietsText.setOnClickListener {
                if (dietsChipGroup.visibility == View.GONE) {
                    dietsChipGroup.visibility = View.VISIBLE
                    animateBlockOpenMark(binding.dietBlockMark)
                } else {
                    dietsChipGroup.visibility = View.GONE
                    animateBlockCloseMark(binding.dietBlockMark)
                }
            }

            intolerancesText.setOnClickListener {
                if (intolerancesChipGroup.visibility == View.GONE) {
                    intolerancesChipGroup.visibility = View.VISIBLE
                    animateBlockOpenMark(binding.intoleranceBlockMark)
                } else {
                    intolerancesChipGroup.visibility = View.GONE
                    animateBlockCloseMark(binding.intoleranceBlockMark)
                }
            }
        }
    }

    private fun animateBlockOpenMark(cardView: MaterialCardView) {
        val rotate = RotateAnimation(
             180f, 0f,
            Animation.RELATIVE_TO_SELF,
            0.5F,
            Animation.RELATIVE_TO_SELF,
            0.5F)
        rotate.duration = 300
        rotate.fillAfter = true
        cardView.startAnimation(rotate)
    }

    private fun animateBlockCloseMark(cardView: MaterialCardView) {
        val rotate = RotateAnimation(0f, 180f,
            Animation.RELATIVE_TO_SELF,
            0.5F,
            Animation.RELATIVE_TO_SELF,
            0.5F)
        rotate.duration = 300
        rotate.fillAfter = true
        cardView.startAnimation(rotate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
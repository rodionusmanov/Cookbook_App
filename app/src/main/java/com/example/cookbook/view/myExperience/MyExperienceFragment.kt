package com.example.cookbook.view.myExperience

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.FragmentFavoriteBinding
import com.example.cookbook.databinding.FragmentMyExperienceBinding

class MyExperienceFragment : Fragment() {

    private var _binding: FragmentMyExperienceBinding? = null
    private val binding: FragmentMyExperienceBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyExperienceBinding.inflate(inflater, container, false)
        return binding.root
    }
}
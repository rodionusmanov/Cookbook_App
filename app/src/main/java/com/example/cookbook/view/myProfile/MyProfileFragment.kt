package com.example.cookbook.view.myProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.FragmentMyExperienceBinding

class MyProfileFragment : Fragment() {

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

    override fun onResume() {
        super.onResume()
        Log.d("@@@", "MyProfile is now resumed")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("@@@", "MyProfile is now hidden: $hidden")
    }
}
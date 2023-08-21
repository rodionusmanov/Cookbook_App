package com.example.cookbook.view.myProfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.FragmentProfileEditBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private var profileUpdatedListener: OnProfileUpdatedListener? = null

    companion object {
        fun newInstance() : EditProfileFragment {
            return EditProfileFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProfileUpdatedListener) {
            profileUpdatedListener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        initExitButton()
        return binding.root
    }

    private fun initExitButton() {
        with(binding) {
            exitButton.setOnClickListener {
                val name = nameEdit.editText?.text.toString()
                val secondName = secondNameEdit.editText?.text.toString()
                saveProfile(name, secondName)
                profileUpdatedListener?.onProfileUpdated(name, secondName)
            }
        }

    }

    private fun saveProfile(name: String, secondName: String) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
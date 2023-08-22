package com.example.cookbook.view.myProfile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.FragmentProfileEditBinding
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class EditProfileFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private var profileUpdatedListener: OnProfileUpdatedListener? = null

    private val model: MyProfileViewModel by activityViewModel()

    companion object {
        fun newInstance() : EditProfileFragment {
            return EditProfileFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent is OnProfileUpdatedListener) {
            profileUpdatedListener = parent
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        initExitButton()
        initEditText()
        return binding.root
    }

    private fun initEditText() {
        with(binding) {
            val nameEdit = nameEdit.editText as TextInputEditText
            nameEdit.setText(model.getProfileName())

            val nameSecondName = secondNameEdit.editText as TextInputEditText
            nameSecondName.setText(model.getProfileSecondName())
        }
    }

    private fun initExitButton() {
        with(binding) {
            exitButton.setOnClickListener {
                val name = nameEdit.editText?.text.toString()
                val secondName = secondNameEdit.editText?.text.toString()
                saveProfile(name, secondName)
                profileUpdatedListener?.onProfileUpdated(name, secondName)
                parentFragmentManager.beginTransaction()
                    .remove(this@EditProfileFragment)
                    .commit()
            }
        }
    }

    private fun saveProfile(name: String, secondName: String) {
        model.saveProfile(name, secondName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
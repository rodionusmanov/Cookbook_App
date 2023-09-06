package com.example.cookbook.view.myProfile

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import coil.load
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentProfileEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditProfileFragment : Fragment() {

    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!

    private var profileUpdatedListener: OnProfileUpdatedListener? = null
    private var imageUri: Uri? = null
    private var avatarUri: Uri? = null

    private val model: MyProfileViewModel by activityViewModel()

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                continueTakingPhotoFromCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.profile_decline_camera_permission),
                    Toast.LENGTH_LONG
                )
            }
        }

    companion object {
        fun newInstance(): EditProfileFragment {
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
        initEditText()
        initFAB()
        loadAvatar()
        return binding.root
    }

    private fun loadAvatar() {
        val file = File(requireContext().filesDir, "avatar_image.jpg")
        if (file.exists()) {
            binding.avatarImage.load(file) { crossfade(true) }
        }
    }

    private fun initFAB() {
        binding.editAvatar.setOnClickListener {
            val view = layoutInflater.inflate(
                R.layout.bottom_sheet_dialog_avatar_edit,
                binding.editAvatar.parent as ViewGroup?,
                false
            )
            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(view)

            view.findViewById<LinearLayout>(R.id.camera_button).setOnClickListener {
                takePhotoFromCamera()
                dialog.dismiss()
            }

            view.findViewById<LinearLayout>(R.id.gallery_button).setOnClickListener {
                choosePhotoFromGallery()
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private val choosePhotoFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                avatarUri = uri
            }
            binding.avatarImage.load(avatarUri) { crossfade(true) }
            saveImage(avatarUri)
        }

    private fun saveImage(uri: Uri?) {
        val inputStream: InputStream? = uri?.let {
            requireContext().contentResolver.openInputStream(it)
        }
        val file = File(requireContext().filesDir, "avatar_image.jpg")

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
    }

    private val takePhotoFromCameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                val file = File(requireContext().filesDir, "avatar_image.jpg")
                val sourceFile = File(requireContext().externalCacheDir, "temp_image.jpg")

                sourceFile.copyTo(file, true)

                avatarUri = Uri.fromFile(file)

                binding.avatarImage.load(avatarUri) { crossfade(true) }

            } else {
                Toast.makeText(requireContext(), getString(R.string.avatar_source_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun choosePhotoFromGallery() {
        choosePhotoFromGalleryLauncher.launch("image/*")
    }

    private fun takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            continueTakingPhotoFromCamera()
        } else {
            requestCameraPermission.launch(android.Manifest.permission.CAMERA)
        }

    }

    private fun continueTakingPhotoFromCamera() {
        val photoFile = File(requireContext().externalCacheDir, "temp_image.jpg")
        imageUri = FileProvider.getUriForFile(
            requireContext(), "com.example.cookbook.provider", photoFile
        )
        takePhotoFromCameraLauncher.launch(imageUri)
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
                profileUpdatedListener?.onProfileUpdated(name, secondName, avatarUri)
                parentFragmentManager.popBackStack()
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
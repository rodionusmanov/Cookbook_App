package com.example.cookbook.view.search.searchDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.DialogFragment
import com.example.cookbook.databinding.FragmentSearchDialogBinding

class SearchDialogFragment : DialogFragment() {

    private var _binding: FragmentSearchDialogBinding? = null
    private val binding get() = _binding!!

//    private val blur: BlurEffect by inject()

    var listener: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        blur.setBlur(16f)
        initViews()
    }

    private fun initViews() {

        with(binding) {
            searchEt.requestFocus()
            searchEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.isNullOrEmpty()) {
                        clearIv.visibility = View.GONE
                    } else {
                        clearIv.visibility = View.VISIBLE
                    }
                }
            })

            searchEt.setOnEditorActionListener { _, key, _ ->
                if (key == EditorInfo.IME_ACTION_SEARCH && !searchEt.text.isNullOrEmpty()) {
                    searchWord()
                }
                true
            }
            clearIv.setOnClickListener {
                searchEt.text?.clear()
            }
            searchBtn.setOnClickListener {
                searchWord()
            }
        }
    }

    private fun searchWord() {
        listener?.invoke(binding.searchEt.text.toString())
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDestroy() {
        listener = null
        _binding = null
//        blur.setBlur(0.01f)
        super.onDestroy()
    }

    companion object {
        fun newInstance() = SearchDialogFragment()
    }
}
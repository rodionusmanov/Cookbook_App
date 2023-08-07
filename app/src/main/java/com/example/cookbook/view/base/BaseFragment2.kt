package com.example.cookbook.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.cookbook.R
import com.example.cookbook.databinding.LayoutLoadingBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.network.NetworkRepository
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

typealias Inflate<F> = (LayoutInflater, ViewGroup?, Boolean) -> F

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment2<T: AppState, I, VB: ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _bindingLoading: LayoutLoadingBinding? = null
    private val bindingLoading get() = _bindingLoading!!

    private val networkRepository: NetworkRepository by inject()
    private var isNetworkAvailable: Boolean = true

    private var _binding : VB? = null
    val binding : VB get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        _bindingLoading = LayoutLoadingBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToNetworkChange()
    }

    private fun subscribeToNetworkChange() {

        var snackbar: Snackbar? = null

        networkRepository.getNetworkStatusLiveData().observe(viewLifecycleOwner) {
            isNetworkAvailable = it
            if (!isNetworkAvailable) {
                snackbar = Snackbar.make(requireView(), R.string.dialog_message_device_is_offline,
                    Snackbar.LENGTH_INDEFINITE)
                snackbar?.show()
            } else {
                snackbar?.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bindingLoading = null
    }

    protected fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success<*> -> {
                showWorkingView()
                val data = appState.data as I
                setupData(data)
            }
            is AppState.Loading -> {
                showViewLoading()
            }
            is AppState.Error -> {
                showWorkingView()
                showErrorDialog(appState.error.message)
            }
        }
    }

    abstract fun setupData(data: I)

    private fun showViewLoading() {
        bindingLoading.loadingLayout.visibility = View.VISIBLE
    }

    abstract fun showErrorDialog(message: String?)

    private fun showWorkingView() {
        bindingLoading.loadingLayout.visibility = View.GONE
    }
}

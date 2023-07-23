package com.example.cookbook.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.cookbook.R
import com.example.cookbook.databinding.LayoutLoadingBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.repository.network.NetworkRepository
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

abstract class BaseFragment<T: AppState, I> : Fragment() {

    private var _bindingLoading: LayoutLoadingBinding? = null
    private val bindingLoading get() = _bindingLoading!!

    private val networkRepository: NetworkRepository by inject()
    private var isNetworkAvailable: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindingLoading = LayoutLoadingBinding.inflate(layoutInflater)
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
        _bindingLoading = null
    }

    protected fun renderData(appState: T) {
        when (appState) {
            is AppState.Success<*> -> {
                showWorkingView()
                val data = appState.data as List<I>
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

    private fun showViewLoading() {
        bindingLoading.loadingLayout.visibility = View.VISIBLE
    }

    abstract fun showErrorDialog(message: String?)

    abstract fun setupData(data: List<I>)

    private fun showWorkingView() {
        bindingLoading.loadingLayout.visibility = View.GONE
    }
}
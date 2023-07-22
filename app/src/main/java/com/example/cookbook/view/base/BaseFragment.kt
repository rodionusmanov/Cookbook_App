package com.example.cookbook.view.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.LayoutLoadingBinding
import com.example.cookbook.model.AppState

abstract class BaseFragment<T: AppState, I> : Fragment() {

    private var _bindingLoading: LayoutLoadingBinding? = null

    private val bindingLayout get() = _bindingLoading!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _bindingLoading = LayoutLoadingBinding.inflate(layoutInflater)
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
        bindingLayout.loadingLayout.visibility = View.VISIBLE
    }

    abstract fun showErrorDialog(message: String?)

    abstract fun setupData(data: List<I>)

    private fun showWorkingView() {
        bindingLayout.loadingLayout.visibility = View.GONE
    }
}
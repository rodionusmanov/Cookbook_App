package com.example.cookbook.view.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentSearchBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.utils.BUNDLE_DISH_TYPE
import com.example.cookbook.utils.BUNDLE_SEARCH_QUERY
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.search.searchResult.SearchResultFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<AppState, List<BaseRecipeData>, FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    private lateinit var model: SearchViewModel

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private fun setSearchQuery(query: String) {
        model.searchRecipeRequest(query, "")
        binding.searchView.setQuery(query, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setupSearchView()
        handleBundle(arguments)
    }

    private fun handleBundle(arguments: Bundle?) {
        arguments?.let {
            val searchQuery = it.getString(BUNDLE_SEARCH_QUERY)
            val dishType = it.getString(BUNDLE_DISH_TYPE)

            when {
                searchQuery != null -> setSearchQuery(searchQuery)
                dishType != null -> setDishTypeQuery(dishType)
            }
        }
    }

    private fun setDishTypeQuery(dishType: String) {
        model.searchRandomRecipesByDishTypes(dishType)
    }

    private fun initViewModel() {
        val viewModel by viewModel<SearchViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect {
                    renderData(it)
                }
            }
        }
    }

    private fun setupSearchView() {

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        model.searchRecipeRequest(it, "")
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            }
        )
    }

    override fun setupData(data: List<BaseRecipeData>) {
        when (val firstItem = data.firstOrNull()) {
            is SearchRecipeData -> setupSearchData(data.filterIsInstance<SearchRecipeData>())
            else -> {
                showErrorDialog("Incorrect data type: ${firstItem?.javaClass?.name}")
            }
        }
    }

    private fun setupSearchData(searchData: List<SearchRecipeData>) {
        val fragment = SearchResultFragment.newInstance(searchData)
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.search_fragment_container, fragment)
            .commit()
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
    }
}
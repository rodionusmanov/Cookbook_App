package com.example.cookbook.view.search

import android.os.Bundle
import android.util.Log
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
import org.koin.android.ext.android.inject

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
        initArgumentsFlow()
        setupSearchView()
    }

    private fun initArgumentsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d("@@@", "Start collecting arguments")
            val initialArgs = model.argumentsFlow.value
            if (initialArgs != null) {
                handleBundle(initialArgs)
            }
            model.argumentsFlow.collect{args ->
                Log.d("@@@", "Collect arguments $args")
                handleBundle(args)
            }
        }
    }

    private fun handleBundle(arguments: Bundle?) {
        arguments?.let {
            val searchQuery = it.getString(BUNDLE_SEARCH_QUERY)
            val dishType = it.getString(BUNDLE_DISH_TYPE)

            Log.d("@@@", "Handling arguments: searchQuery = $searchQuery, dishType = $dishType")

            when {
                searchQuery != null -> setSearchQuery(searchQuery)
                dishType != null -> setDishTypeQuery(dishType)
            }
        } ?: Log.d("@@@", "No arguments to handle")
    }

    private fun setDishTypeQuery(dishType: String) {
        model.searchRandomRecipesByDishTypes(dishType)
        binding.searchView.setQuery("", false)
    }

    private fun initViewModel() {
        val viewModel: SearchViewModel by inject()
        model = viewModel
        Log.d("@@@", "ViewModel hash: ${model.hashCode()}")
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
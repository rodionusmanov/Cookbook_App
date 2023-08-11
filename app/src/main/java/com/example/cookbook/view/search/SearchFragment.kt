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
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.mainActivity.MainActivity
import com.example.cookbook.view.search.searchResult.SearchResultFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<AppState, List<BaseRecipeData>, FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    private lateinit var model: SearchViewModel

    companion object {

        private const val SEARCH_RECIPE_LIST_KEY = "SEARCH_DATA_KEY"

        fun newInstance(searchData: List<SearchRecipeData>): SearchFragment {
            return SearchFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(SEARCH_RECIPE_LIST_KEY, ArrayList(searchData))
                }
            }
        }
    }

    private fun setSearchQuery(query: String) {
        model.searchRecipeRequest(query, "")
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        setupSearchView()
        super.onViewCreated(view, savedInstanceState)
        //val query = arguments?.getString("search_query")
        //query?.let {setSearchQuery(it)}
    }

    private fun initViewModel() {
        val viewModel by viewModel<SearchViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
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

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).printBackStack()
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
        childFragmentManager
            .beginTransaction()
            .replace(R.id.search_fragment_container, fragment)
            .commit()
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
    }
}
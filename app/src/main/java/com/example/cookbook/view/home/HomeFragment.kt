package com.example.cookbook.view.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentHomeBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.home.randomRecipe.RandomRecipesListFragment
import com.example.cookbook.view.search.searchResult.SearchResultFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragment<AppState, List<BaseRecipeData>, FragmentHomeBinding>(
        FragmentHomeBinding::inflate
    ) {

    private lateinit var model: HomeViewModel

    private val selectedIngredients = mutableSetOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        setupSearchView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupSearchView() {

        binding.searchView.setOnQueryTextListener(
            object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        model.searchRecipeRequest(it, selectedIngredients.joinToString(","))
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            }
        )
    }

    private fun initViewModel() {
        val viewModel by viewModel<HomeViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
        model.getRandomRecipes()
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
    }

    override fun setupData(data: List<BaseRecipeData>) {
        when (val firstItem = data.firstOrNull()) {
            is SearchRecipeData -> setupSearchData(data.filterIsInstance<SearchRecipeData>())
            is RandomRecipeData -> setupRandomData(data.filterIsInstance<RandomRecipeData>())
            else -> {
                showErrorDialog("Incorrect data type: ${firstItem?.javaClass?.name}")
            }
        }
    }

    private fun setupRandomData(randomData: List<RandomRecipeData>) {
        val fragment = RandomRecipesListFragment.newInstance(randomData)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_fragment_container, fragment)
            .commit()
    }

    private fun setupSearchData(searchData: List<SearchRecipeData>) {
        val fragment = SearchResultFragment.newInstance(searchData)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.search_fragment_container, fragment)
            .commit()
    }
}
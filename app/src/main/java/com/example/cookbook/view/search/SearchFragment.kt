package com.example.cookbook.view.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.databinding.FragmentSearchBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.utils.BUNDLE_DISH_TYPE
import com.example.cookbook.utils.BUNDLE_SEARCH_QUERY
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.mainActivity.MainActivity
import com.example.cookbook.view.search.searchResult.SearchResultAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : BaseFragment<AppState, List<BaseRecipeData>, FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    private val model: SearchViewModel by activityViewModel()
    private val adapter: SearchResultAdapter by lazy { SearchResultAdapter() }
    private var navigationManager: NavigationManager? = null

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) = SearchFragment().apply {
            arguments = bundle
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    private fun setSearchQuery(query: String) {
        model.searchRecipeRequest(query, "")
        binding.searchView.setQuery(query, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resultRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.resultRecyclerView.adapter = adapter

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
            model.argumentsFlow.collect { args ->
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
        arguments?.getString("search_query")?.let {
            binding.searchView.setQuery(it, false)
            model.searchRecipeRequest(
                it,
                ""
            )
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
    }

    private fun setupSearchView() {

        with(binding) {
            searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let {
                            model.searchRecipeRequest(it, "")
                            hideKeyboard(binding.searchView)

                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        when (newText?.length) {
                            0 -> {
                                with(binding) {
                                    resultRecyclerView.isVisible = false
                                    variousDishesTable.isVisible = true
                                }
                            }

                            else -> {
                                with(binding) {
                                    resultRecyclerView.isVisible = true
                                    variousDishesTable.isVisible = false
                                }
                            }
                        }
                        return true
                    }
                }
            )

            btnAllFilters.setOnClickListener {
                openAllFiltersFragment()
            }

            cardBreakfast.setOnClickListener {
                model.searchRandomRecipesByDishTypes("breakfast")
                binding.searchView.setQuery("breakfast", false)
            }
        }
    }

    override fun setupData(data: List<BaseRecipeData>) {
        adapter.submitList(data)


        initFavoriteListeners()

        adapter.listener = { recipe ->
            openRecipeInfoFragment(recipe.id)
        }
    }

    private fun initFavoriteListeners() {
        adapter.listenerOnSaveRecipe = { recipe ->
//            model.insertNewRecipeToDataBase(recipe)
        }

        adapter.listenerOnRemoveRecipe = { recipe ->
//            model.deleteRecipeFromData(recipe.id)
        }
    }

    private fun openRecipeInfoFragment(recipeId: Int) {
        navigationManager?.openRecipeInfoFragment(recipeId)
    }

    private fun openAllFiltersFragment(){
        navigationManager?.openAllFiltersFragment()
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun hideKeyboard(view: View) {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
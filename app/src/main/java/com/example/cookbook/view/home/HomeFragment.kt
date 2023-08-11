package com.example.cookbook.view.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentHomeBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.home.randomRecipe.RandomRecipesListFragment
import com.example.cookbook.view.mainActivity.MainActivity
import com.example.cookbook.view.search.SearchFragment
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
        //initRandomRecipeFragment()
        initDishTypeCards()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initDishTypeCards() {
        binding.cardBreakfast.setOnClickListener { view ->
            Log.d("Navigation", "Card clicked, navigating to SearchFragment")
            val navController = findNavController()
            val action = R.id.action_navigation_home_to_searchFragment
            val bundle = bundleOf("search_query" to "breakfast")
            navController.navigate(action, bundle)
            (activity as MainActivity).setSelectedNavigationItem(R.id.navigation_search_recipe)
            }
        }

    override fun onResume() {
        super.onResume()
        Log.d("@@@", "HomeFragment resumed")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("@@@", "HomeFragment onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d("@@@", "HomeFragment onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("@@@", "HomeFragment onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("@@@", "HomeFragment onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("@@@", "HomeFragment onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("@@@", "HomeFragment onDestroy")
    }

    private fun initRandomRecipeFragment() {
        val fragment = RandomRecipesListFragment.newInstance()
        childFragmentManager
            .beginTransaction()
            .replace(R.id.random_recipe_container, fragment)
            .commit()
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
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
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
        val searchFragment = SearchFragment.newInstance(searchData)
        childFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, searchFragment)
            .commit()
    }
}
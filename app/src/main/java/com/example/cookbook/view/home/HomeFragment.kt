package com.example.cookbook.view.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentHomeBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.utils.BUNDLE_DISH_TYPE
import com.example.cookbook.utils.DISH_TYPE_BREAKFAST
import com.example.cookbook.utils.DISH_TYPE_DESSERT
import com.example.cookbook.utils.DISH_TYPE_MAIN_COURSE
import com.example.cookbook.utils.DISH_TYPE_SALAD
import com.example.cookbook.utils.DISH_TYPE_SIDE_DISH
import com.example.cookbook.utils.DISH_TYPE_SNACK
import com.example.cookbook.utils.FRAGMENT_SEARCH
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.home.healthyRandomRecipe.HealthyRandomRecipeListFragment
import com.example.cookbook.view.home.randomCuisineRecipes.RandomCuisineRecipeListFragment
import com.example.cookbook.view.home.randomRecipe.RandomRecipesListFragment
import com.example.cookbook.view.mainActivity.MainActivity
import com.example.cookbook.view.search.SearchFragment
import com.example.cookbook.view.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragment<AppState, String, FragmentHomeBinding>(
        FragmentHomeBinding::inflate
    ) {

    private lateinit var model: HomeViewModel
    private var navigationManager: NavigationManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        setupSearchView()
        //initRandomRecipeFragment()
        //initHealthyRandomRecipeFragment()
        initDishTypeCards()
        initRandomCuisineFragment()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRandomCuisineFragment() {
        val existingFragment = childFragmentManager.findFragmentById(R.id.random_cuisine_recipes_container)
        if (existingFragment == null) {
            val fragment = RandomCuisineRecipeListFragment.newInstance()
            childFragmentManager
                .beginTransaction()
                .replace(R.id.random_cuisine_recipes_container, fragment)
                .commit()
        }
    }

    private fun initHealthyRandomRecipeFragment() {
        val existingFragment = childFragmentManager.findFragmentById(R.id.healthy_random_recipes_container)
        if (existingFragment == null) {
            val fragment = HealthyRandomRecipeListFragment.newInstance()
            childFragmentManager
                .beginTransaction()
                .replace(R.id.healthy_random_recipes_container, fragment)
                .commit()
        }
    }

    private fun initDishTypeCards() {
        binding.cardBreakfast.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_BREAKFAST)
            }
        binding.cardSideDish.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_SIDE_DISH)
        }
        binding.cardMainCourse.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_MAIN_COURSE)
        }
        binding.cardSalads.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_SALAD)
        }
        binding.cardSnack.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_SNACK)
        }
        binding.cardDessert.setOnClickListener {
            openSearchFragmentWithQuery(BUNDLE_DISH_TYPE, DISH_TYPE_DESSERT)
        }
    }

    private fun initRandomRecipeFragment() {
        val existingFragment = childFragmentManager.findFragmentById(R.id.random_recipe_container)
        if (existingFragment == null) {
            val fragment = RandomRecipesListFragment.newInstance()
            childFragmentManager
                .beginTransaction()
                .replace(R.id.random_recipe_container, fragment)
                .commit()
        }
    }

    private fun setupSearchView() {

        binding.searchView.setOnQueryTextListener(
            object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        openSearchFragmentWithQuery("search_query", it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            }
        )
    }

    private fun openSearchFragmentWithQuery(queryKey: String, query: String) {

        val args = Bundle().apply {
            putString(queryKey, query)
        }
        val searchViewModel: SearchViewModel by inject()
        Log.d("@@@", "ViewModel hash: ${searchViewModel.hashCode()}")
        searchViewModel.updateArguments(args)
        val searchFragment = SearchFragment.newInstance()
        navigationManager?.switchFragment(FRAGMENT_SEARCH, searchFragment, addToBackStack = true)
    }

    private fun initViewModel() {
        val viewModel by viewModel<HomeViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
        requestJokeText()
    }

    private fun requestJokeText() {
        model.getJokeText()
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
    }

    override fun setupData(data: String) {
        binding.jokeText.text = data
    }
}
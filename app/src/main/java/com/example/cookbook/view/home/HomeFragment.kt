package com.example.cookbook.view.home

import android.content.Context
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
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.utils.FragmentUtils
import com.example.cookbook.utils.navigation.NavigationUtils
import com.example.cookbook.utils.navigation.OnFragmentSwitchListener
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.home.randomRecipe.RandomRecipesListFragment
import com.example.cookbook.view.search.SearchFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragment<AppState, List<BaseRecipeData>, FragmentHomeBinding>(
        FragmentHomeBinding::inflate
    ) {

    private lateinit var model: HomeViewModel
    private val selectedIngredients = mutableSetOf<String>()
    private var listener: OnFragmentSwitchListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentSwitchListener){
            listener = context
        } else {
            throw RuntimeException("$context don't implement OnFragmentSwitchedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        setupSearchView()
        //initRandomRecipeFragment()
        initDishTypeCards()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initDishTypeCards() {
        binding.cardBreakfast.setOnClickListener {
            openSearchFragmentWithQuery("search_query","breakfast")
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
        val searchFragment = FragmentUtils.obtainFragment(
            fragmentManager = parentFragmentManager,
            fragmentClass = SearchFragment::class.java,
            newInstance = {SearchFragment.newInstance()}
        )
        val listener = activity as? OnFragmentSwitchListener
            ?: throw RuntimeException("Activity don't implement OnFragmentSwitchedListener")

        NavigationUtils.navigateToSearchFragmentWithQuery(
            fragmentManager = parentFragmentManager,
            listener = listener,
            containerId = R.id.main_container,
            destinedFragment = searchFragment,
            queryKey = queryKey,
            queryValue = query,
            tag = "search_recipe"
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
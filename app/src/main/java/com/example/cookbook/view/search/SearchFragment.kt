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
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.databinding.FragmentSearchBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.utils.BUNDLE_DISH_TYPE
import com.example.cookbook.utils.BUNDLE_DISH_TYPE_FILTER
import com.example.cookbook.utils.BUNDLE_INCLUDE_INGREDIENT_FILTER
import com.example.cookbook.utils.BUNDLE_SEARCH_QUERY
import com.example.cookbook.utils.DISH_TYPE_APPETIZER
import com.example.cookbook.utils.DISH_TYPE_BEVERAGE
import com.example.cookbook.utils.DISH_TYPE_BREAD
import com.example.cookbook.utils.DISH_TYPE_BREAKFAST
import com.example.cookbook.utils.DISH_TYPE_DESSERT
import com.example.cookbook.utils.DISH_TYPE_DRINK
import com.example.cookbook.utils.DISH_TYPE_MAIN_COURSE
import com.example.cookbook.utils.DISH_TYPE_SALAD
import com.example.cookbook.utils.DISH_TYPE_SAUCE
import com.example.cookbook.utils.DISH_TYPE_SIDE_DISH
import com.example.cookbook.utils.DISH_TYPE_SNACK
import com.example.cookbook.utils.DISH_TYPE_SOUP
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.mainActivity.MainActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : BaseFragment<AppState, List<BaseRecipeData>, FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    private val model: SearchViewModel by activityViewModel()
    private val adapter: SearchResultAdapter by lazy { SearchResultAdapter() }
    private var navigationManager: NavigationManager? = null
    private var isLoading = false

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
        model.searchRecipeRequest(request = query)
        binding.searchView.setQuery(query, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initArgumentsFlow()
        initView()
    }

    private fun initArgumentsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            val initialArgs = model.argumentsFlow.value
            if (initialArgs != null) {
                handleBundle(initialArgs)
            }
            model.argumentsFlow.collect { args ->
                handleBundle(args)
            }
        }
    }

    private fun handleBundle(arguments: Bundle?) {
        arguments?.let {
            val searchQuery = it.getString(BUNDLE_SEARCH_QUERY)
            val dishType = it.getString(BUNDLE_DISH_TYPE)
            val includeFilter = it.getString(BUNDLE_INCLUDE_INGREDIENT_FILTER)
            val dishTypeFilter = it.getString(BUNDLE_DISH_TYPE_FILTER)

            when {
                searchQuery != null -> setSearchQuery(searchQuery)
                dishType != null -> setDishTypeQuery(dishType)
                includeFilter != null || dishTypeFilter != null -> setFilterQuery(
                    includeFilter, dishTypeFilter
                )
            }
        } ?: Log.d("@@@", "No arguments to handle")
    }

    private fun setFilterQuery(includeList: String?, dishType: String?) {
        model.searchRecipeRequest(
            includeIngredients = includeList,
            dishType = dishType
        )
        with(binding) {
            searchView.setQuery("Filter search", false)
            if (searchView.query.isNotEmpty()) {
                variousDishes.variousDishesTableContainer.isVisible = false
            }
        }
    }

    private fun setDishTypeQuery(dishType: String) {
        model.searchRecipeRequest(dishType =  dishType)
        with(binding) {
            searchView.setQuery(dishType, false)
            variousDishes.variousDishesTableContainer.isVisible = false
        }

    }

    private fun initViewModel() {
        arguments?.getString(BUNDLE_SEARCH_QUERY)?.let {
            binding.searchView.setQuery(it, false)
            model.searchRecipeRequest(
                it)
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
    }

    private fun initView() {

        with(binding) {
            searchView.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let {
                            model.searchRecipeRequest(it)
                            hideKeyboard(binding.searchView)

                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        when (newText?.length) {
                            0 -> {
                                with(binding) {
                                    variousDishes.variousDishesTableContainer.isVisible = true
                                }
                            }

                            else -> {
                                with(binding) {
                                    variousDishes.variousDishesTableContainer.isVisible = false
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

            val linearLayout= LinearLayoutManager(context)
            binding.resultRecyclerView.layoutManager =linearLayout
            binding.resultRecyclerView.adapter = adapter

            resultRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = linearLayout.childCount
                    val totalItemCount = linearLayout.itemCount
                    val lastVisibleItem = linearLayout.findLastVisibleItemPosition()

                    if(!isLoading && totalItemCount == lastVisibleItem + visibleItemCount) {
                        isLoading = true
                        model.loadNextPage()
                    }
                }
            })


            variousDishes.dishesTableThirdLineContainer.isVisible = true
            variousDishes.dishesTableFourthLineContainer.isVisible = true

            variousDishes.cardBreakfast.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_BREAKFAST)
            }
            variousDishes.cardSideDish.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_SIDE_DISH)
            }
            variousDishes.cardMainCourse.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_MAIN_COURSE)
            }
            variousDishes.cardSalads.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_SALAD)
            }
            variousDishes.cardSnack.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_SNACK)
            }
            variousDishes.cardDessert.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_DESSERT)
            }
            variousDishes.cardSoup.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_SOUP)
            }
            variousDishes.cardAppetizer.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_APPETIZER)
            }
            variousDishes.cardBeverage.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_BEVERAGE)
            }
            variousDishes.cardSauce.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_SAUCE)
            }
            variousDishes.cardBread.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_BREAD)
            }
            variousDishes.cardDrink.setOnClickListener {
                setDishTypeQuery(DISH_TYPE_DRINK)
            }
        }
    }

    override fun setupData(data: List<BaseRecipeData>) {
        if(model.isInitialLoad) {
            adapter.submitList(data)
        } else {
            adapter.submitList(data)
            isLoading = false
        }
        adapter.listener = { recipe ->
            openRecipeInfoFragment(recipe.id)
        }
    }

    private fun openRecipeInfoFragment(recipeId: Int) {
        navigationManager?.openRecipeInfoFragment(recipeId)
    }

    fun openAllFiltersFragment() {
        navigationManager?.openAllFiltersFragment()
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun hideKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun checkRecipeExistenceInDatabase(){

    }
}
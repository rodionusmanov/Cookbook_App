package com.example.cookbook.view.search.searchResult

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.databinding.FragmentSearchResultBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.utils.parcelableArrayList
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.mainActivity.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchResultFragment :
    BaseFragment<AppState, List<SearchRecipeData>, FragmentSearchResultBinding>(
        FragmentSearchResultBinding::inflate
    ) {

    private lateinit var model: SearchResultViewModel

    private val adapter: SearchResultAdapter by lazy { SearchResultAdapter() }

    private lateinit var favoriteRecipes: List<BaseRecipeData>
    private var navigationManager: NavigationManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    companion object {
        private const val SEARCH_DATA_KEY = "SEARCH_DATA_KEY"

        fun newInstance(searchData: List<SearchRecipeData>): SearchResultFragment {
            return SearchResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(SEARCH_DATA_KEY, ArrayList(searchData))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.parcelableArrayList<SearchRecipeData>(SEARCH_DATA_KEY)?.let { searchData ->
            setupData(searchData)
        }
        initViewModel()
        initFavoriteRecipes()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
    }

    override fun setupData(data: List<SearchRecipeData>) {
        adapter.setData(data)

        initFavoriteListeners()

        adapter.listener = { recipe ->
            openRecipeInfoFragment(recipe.id)
        }

        binding.resultRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.resultRecyclerView.adapter = adapter
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

    private fun initViewModel() {
        val viewModel by viewModel<SearchResultViewModel>()
        model = viewModel
    }

    private fun initFavoriteRecipes() {
        /*model.getAllLocalRecipes().observe(viewLifecycleOwner) {
            favoriteRecipes = it
        }*/
    }
}
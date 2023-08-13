package com.example.cookbook.view.search.searchResult

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.databinding.FragmentSearchResultBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.utils.navigation.NavigationUtils
import com.example.cookbook.utils.navigation.OnFragmentSwitchListener
import com.example.cookbook.utils.parcelableArrayList
import com.example.cookbook.view.base.BaseFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchResultFragment :
    BaseFragment<AppState, List<SearchRecipeData>, FragmentSearchResultBinding>(
        FragmentSearchResultBinding::inflate
    ) {

    private lateinit var model: SearchResultViewModel

    private val adapter: SearchResultAdapter by lazy { SearchResultAdapter() }

    private lateinit var favoriteRecipes: List<BaseRecipeData>

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
            model.insertNewRecipeToDataBase(recipe)
        }

        adapter.listenerOnRemoveRecipe = { recipe ->
            model.deleteRecipeFromData(recipe.id)
        }
    }

    private fun openRecipeInfoFragment(recipeId: Int) {
        val listener = activity as? OnFragmentSwitchListener
            ?: throw RuntimeException("Activity don't implement OnFragmentSwitchedListener")
        NavigationUtils.openRecipeInfoFragment(
            requireActivity().supportFragmentManager,
            listener = listener,
            recipeId
        )
    }

    private fun initViewModel() {
        val viewModel by viewModel<SearchResultViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
    }

    private fun initFavoriteRecipes() {
        model.getAllLocalRecipes().observe(viewLifecycleOwner) {
            favoriteRecipes = it
        }
    }
}
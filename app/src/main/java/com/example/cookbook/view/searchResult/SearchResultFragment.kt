package com.example.cookbook.view.searchResult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentSearchResultBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.utils.ID
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.searchRecipe.ISaveRecipe
import com.example.cookbook.view.searchRecipe.SearchRecipeAdapter
import com.example.cookbook.viewModel.searchRecipe.SearchResultViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchResultFragment : BaseFragment<AppState>() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding: FragmentSearchResultBinding
        get() {
            return _binding!!
        }

    private lateinit var model: SearchResultViewModel

    private val adapter: SearchRecipeAdapter by lazy { SearchRecipeAdapter(callbackSaveItem) }

    private lateinit var favoriteRecipes: List<SearchRecipeData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        initViewModel()
        initFavoriteRecipes()

        return binding.root
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Ошибка {$message}", Toast.LENGTH_LONG).show()
    }

    override fun setupData(data: Any?) {
        val searchData = data as List<SearchRecipeData>
        adapter.setData(searchData)
        adapter.listener = {
            findNavController().navigate(
                R.id.action_navigation_search_recipe_to_recipeInfoFragment,
                Bundle().apply {
                    putInt(ID, it.id)
                })
        }
        binding.resultRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.resultRecyclerView.adapter = adapter
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

    private val callbackSaveItem = object : ISaveRecipe {
        override fun saveRecipe(recipe: SearchRecipeData) {
            model.insertNewRecipeToDataBase(recipe)
        }
    }

    private fun initFavoriteRecipes() {
        model.getAllLocalRecipes().observe(viewLifecycleOwner) {
            favoriteRecipes = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
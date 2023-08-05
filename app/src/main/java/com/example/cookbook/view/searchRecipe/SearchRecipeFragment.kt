package com.example.cookbook.view.searchRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentSearchRecipeBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.utils.ID
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.viewModel.searchRecipe.SearchRecipeViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchRecipeFragment : BaseFragment<AppState, List<SearchRecipeData>>() {

    private var _binding: FragmentSearchRecipeBinding? = null
    private val binding: FragmentSearchRecipeBinding
        get() {
            return _binding!!
        }

    private lateinit var model: SearchRecipeViewModel
    private val adapter: SearchRecipeAdapter by lazy { SearchRecipeAdapter(callbackSaveItem) }

    private val selectedIngredients = mutableSetOf<String>()

    private lateinit var favoriteRecipes: List<SearchRecipeData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchRecipeBinding.inflate(inflater, container, false)

        initViewModel()
        setupSearchView()
        setupChips()
        initFavoriteRecipes()

        return binding.root
    }


    private fun setupChips() {
        val chipChicken: Chip = binding.chipChicken
        val chipTomato: Chip = binding.chipTomato

        chipChicken.setOnCheckedChangeListener { _, isChecked ->
            handleChipCheck(isChecked, "chicken")
        }

        chipTomato.setOnCheckedChangeListener { _, isChecked ->
            handleChipCheck(isChecked, "tomato")
        }
    }

    private fun handleChipCheck(isChecked: Boolean, ingredient: String) {
        if (isChecked) {
            selectedIngredients.add(ingredient)
        } else {
            selectedIngredients.remove(ingredient)
        }
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
        val viewModel by viewModel<SearchRecipeViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Ошибка {$message}", Toast.LENGTH_LONG).show()
    }

    override fun setupData(data: List<SearchRecipeData>) {
        adapter.setData(data)
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

    private fun initFavoriteRecipes() {
        model.getAllLocalRecipes().observe(viewLifecycleOwner) {
            favoriteRecipes = it
        }
    }

    private val callbackSaveItem = object : ISaveRecipe {
        override fun saveRecipe(recipe: SearchRecipeData) {
            model.insertNewRecipeToDataBase(recipe)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.cookbook.view.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.databinding.FragmentFavoriteBinding
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.view.searchRecipe.ISaveRecipe
import com.example.cookbook.viewModel.favorite.FavoriteRecipesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding
        get() {
            return _binding!!
        }

    private lateinit var model: FavoriteRecipesViewModel
    private val adapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(callbackSaveItem) }

    private lateinit var favoriteRecipes: List<SearchRecipeData>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        initViewModel()
        initFavoriteRecipes()
        return binding.root
    }

    private fun initViewModel() {
        val viewModel by viewModel<FavoriteRecipesViewModel>()
        model = viewModel
        model.getAllLocalRecipes().observe(viewLifecycleOwner) {
            favoriteRecipes = it
            setupData(favoriteRecipes)
        }
    }

    private fun setupData(favoriteRecipes: List<SearchRecipeData>) {
        adapter.setData(favoriteRecipes)
        binding.favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.favoriteRecipesRecyclerView.adapter = adapter
    }

    private fun initFavoriteRecipes() {
        model.getAllLocalRecipes().observe(viewLifecycleOwner) {
            favoriteRecipes = it
            Toast.makeText(context, "${favoriteRecipes.size}", Toast.LENGTH_SHORT).show()
        }
    }

    private val callbackSaveItem = object : ISaveRecipe {
        override fun saveRecipe(recipe: SearchRecipeData) {
            model.insertNewRecipeToDataBase(recipe)
        }
    }
}
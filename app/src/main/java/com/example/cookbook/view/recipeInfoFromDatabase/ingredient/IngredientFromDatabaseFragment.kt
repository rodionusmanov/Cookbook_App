package com.example.cookbook.view.recipeInfoFromDatabase.ingredient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cookbook.databinding.FragmentIngredientBinding
import com.example.cookbook.view.recipeInfo.adapters.RecipeIngredientsAdapter
import com.example.cookbook.view.recipeInfoFromDatabase.RecipeInfoFromDatabaseViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class IngredientFromDatabaseFragment : Fragment() {

    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!
    private val adapter: RecipeIngredientsAdapter by lazy {
        RecipeIngredientsAdapter(context?.applicationContext)
    }
    private val viewModel: RecipeInfoFromDatabaseViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ingredients.collect {
                    adapter.submitList(it)
                }
            }
        }
        binding.rvIngredients.adapter = adapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
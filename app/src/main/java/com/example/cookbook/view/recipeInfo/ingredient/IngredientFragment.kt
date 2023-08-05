package com.example.cookbook.view.recipeInfo.ingredient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.FragmentIngredientBinding
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient
import com.example.cookbook.utils.INGREDIENTS
import com.example.cookbook.view.recipeInfo.adapters.IngredientsAdapter

class IngredientFragment : Fragment() {

    private var _binding: FragmentIngredientBinding? = null
    private val binding get() = _binding!!
    private val adapter = IngredientsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @Suppress("DEPRECATION")
    private fun initView() {
        val ingredients =
            arguments?.getParcelableArrayList<ExtendedIngredient>(INGREDIENTS)
        ingredients?.let { adapter.setData(it) }
        with(binding) {
            rvIngredients.adapter = adapter
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
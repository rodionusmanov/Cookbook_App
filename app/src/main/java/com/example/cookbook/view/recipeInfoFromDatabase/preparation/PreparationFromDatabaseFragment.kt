package com.example.cookbook.view.recipeInfo.preparation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.databinding.FragmentPreparationBinding
import com.example.cookbook.model.datasource.DTO.recipeInformation.AnalyzedInstruction
import com.example.cookbook.model.datasource.DTO.recipeInformation.Equipment
import com.example.cookbook.model.datasource.DTO.recipeInformation.Ingredient
import com.example.cookbook.view.recipeInfo.RecipeInfoViewModel
import com.example.cookbook.view.recipeInfo.adapters.UniversalAdapter
import com.example.cookbook.view.recipeInfoFromDatabase.RecipeInfoFromDatabaseViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PreparationFromDatabaseFragment : Fragment() {

    private var _binding: FragmentPreparationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeInfoFromDatabaseViewModel by activityViewModel()

    private val stepsAdapter: UniversalAdapter by inject()
    private val equipmentsAdapter: UniversalAdapter by inject()
    private val ingredientAdapter: UniversalAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreparationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.instructions.collect {
                    initView(it)
                }
            }
        }
    }


    private fun initView(analyzedInstructions: List<AnalyzedInstruction>) {

        val ingredientHorizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val equipmentHorizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val equipmentList = mutableListOf<Equipment>()
        val ingredientsList = mutableListOf<Ingredient>()

        analyzedInstructions.forEach { instruction ->
            instruction.steps.forEach { step ->
                equipmentList.addAll(step.equipment)
                ingredientsList.addAll(step.ingredients)
            }
        }
        stepsAdapter.submitList(analyzedInstructions.first().steps)
        equipmentsAdapter.submitList(equipmentList.distinct())
        ingredientAdapter.submitList(ingredientsList.distinct())

        with(binding) {
            tvEquipments.isVisible = equipmentList.isNotEmpty()
            tvIngredient.isVisible = ingredientsList.isNotEmpty()
            rvIngredientsPreparation.layoutManager = ingredientHorizontalLayoutManager
            rvEquipments.layoutManager = equipmentHorizontalLayoutManager

            rvEquipments.adapter = equipmentsAdapter
            rvIngredientsPreparation.adapter = ingredientAdapter
            rvSteps.adapter = stepsAdapter
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
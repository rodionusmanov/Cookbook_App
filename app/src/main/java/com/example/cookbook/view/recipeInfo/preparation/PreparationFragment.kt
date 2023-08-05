package com.example.cookbook.view.recipeInfo.preparation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.databinding.FragmentPreparationBinding
import com.example.cookbook.model.datasource.DTO.recipeInformation.AnalyzedInstruction
import com.example.cookbook.model.datasource.DTO.recipeInformation.Equipment
import com.example.cookbook.model.datasource.DTO.recipeInformation.Ingredient
import com.example.cookbook.utils.INSTRUCTIONS
import com.example.cookbook.view.recipeInfo.adapters.EquipmentsPreparationAdapter
import com.example.cookbook.view.recipeInfo.adapters.IngredientsPreparationAdapter
import com.example.cookbook.view.recipeInfo.adapters.InstructionsAdapter

class PreparationFragment : Fragment() {

    private var _binding: FragmentPreparationBinding? = null
    private val binding get() = _binding!!
    private val stepsAdapter = InstructionsAdapter()
    private val equipmentsAdapter = EquipmentsPreparationAdapter()
    private val ingredientAdapter = IngredientsPreparationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreparationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @Suppress("DEPRECATION")
    private fun initView() {
        val instructions =
            arguments?.getParcelableArrayList<AnalyzedInstruction>(INSTRUCTIONS)
        instructions?.let {
            stepsAdapter.setData(it.first().steps)
        }

        val ingredientHorizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val equipmentHorizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val equipmentList = mutableListOf<Equipment>()
        val ingredientsList = mutableListOf<Ingredient>()

        instructions?.forEach { instruction ->
            instruction.steps.forEach { step ->
                equipmentList.addAll(step.equipment)
                ingredientsList.addAll(step.ingredients)
            }
        }

        equipmentsAdapter.setData(equipmentList.distinct())
        ingredientAdapter.setData(ingredientsList.distinct())

        with(binding) {
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
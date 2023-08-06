package com.example.cookbook.view.recipeInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cookbook.databinding.FragmentRecipeInfoBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.utils.ID
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.recipeInfo.adapters.RecipeInformationPageAdapter
import com.example.cookbook.viewModel.recipeInfo.RecipeInfoViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeInfoFragment : BaseFragment<AppState, RecipeInformation>() {

    private var _binding: FragmentRecipeInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeInfoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt(ID)

        id?.let { viewModel.recipeInfoRequest(it) }
        lifecycleScope.launch {
            viewModel.stateFlow.collect {
                renderData(it)
            }
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun setupData(data: RecipeInformation) {
        with(binding) {
            tvRecipeInfoTitle.text = data.title
            ivRecipeInfoImage.load(data.image) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16f))
            }
            tvRecipeInfoCalories.text = "${data.calories?.amount} ${data.calories?.unit}"
            tvRecipeInfoProtein.text =
                "${data.protein?.amount}${data.protein?.unit}"
            tvRecipeInfoFat.text = "${data.fat?.amount}${data.fat?.unit}"
            tvRecipeInfoCarbohydrates.text =
                "${data.carbohydrates?.amount}${data.carbohydrates?.unit}"

            viewPager.adapter = RecipeInformationPageAdapter(data, requireActivity())
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = "Ingredient"
                    else -> tab.text = "Preparation"
                }
            }.attach()
        }

    }

    override fun showErrorDialog(message: String?) {
        TODO("Not yet implemented")
    }
}
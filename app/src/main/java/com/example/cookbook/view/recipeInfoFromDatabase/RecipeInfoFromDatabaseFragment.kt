package com.example.cookbook.view.recipeInfoFromDatabase

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentRecipeInfoFromDatabaseBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.utils.ID
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.recipeInfo.RecipeInfoFragment
import com.example.cookbook.view.recipeInfo.adapters.RecipeInformationPageAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeInfoFromDatabaseFragment :
    BaseFragment<AppState, RecipeInformation, FragmentRecipeInfoFromDatabaseBinding>(
        FragmentRecipeInfoFromDatabaseBinding::inflate
    ) {

    companion object {
        fun newInstance(): RecipeInfoFromDatabaseFragment {
            return RecipeInfoFromDatabaseFragment()
        }
    }

    private val viewModel: RecipeInfoFromDatabaseViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = requireArguments().getInt(ID)

        id?.let{viewModel.getRecipeInfoFromDatabase(it)}
        lifecycleScope.launch {
            viewModel.stateFlow.collect {
                renderData(it)
            }
        }
    }

    override fun showErrorDialog(message: String?) {
//        TODO("Not yet implemented")
    }

    override fun setupData(data: RecipeInformation) {

        viewModel.setIngredients(data.extendedIngredients)

        with(binding) {
            chDairyFree.isChecked = data.dairyFree
            chGlutenFree.isChecked = data.glutenFree
            chVegan.isChecked = data.vegan
            chVegetarian.isChecked = data.vegetarian
            chVeryHealthy.isChecked = data.veryHealthy

            iconDairyFree.visibility = if (data.dairyFree) {
                View.VISIBLE
            } else {
                View.GONE
            }
            iconGlutenFree.visibility = if (data.glutenFree) {
                View.VISIBLE
            } else {
                View.GONE
            }
            iconVegan.visibility = if (data.vegan) {
                View.VISIBLE
            } else {
                View.GONE
            }
            iconVegetarian.visibility = if (data.vegetarian) {
                View.VISIBLE
            } else {
                View.GONE
            }
            iconHealthyFood.visibility = if (data.veryHealthy) {
                View.VISIBLE
            } else {
                View.GONE
            }

            tvRecipeInfoTitle.text = data.title
            ivRecipeInfoImage.load(data.image) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16f))
            }
            chCalories.text = "${data.calories?.amount} ${data.calories?.unit}"
            chProtein.text =
                "${resources.getString(R.string.protein)} - ${data.protein?.amount}${data.protein?.unit}"
            chFat.text =
                "${resources.getString(R.string.fat)} - ${data.fat?.amount}${data.fat?.unit}"
            chCarb.text =
                "${resources.getString(R.string.carb)} - ${data.carbohydrates?.amount}${data.carbohydrates?.unit}"

            viewPager.adapter = RecipeInformationPageAdapter(requireActivity())
            viewPager.isUserInputEnabled = false
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = "Ingredient"
                    else -> tab.text = "Preparation"
                }
            }.attach()

            chAddToFavorite.setOnClickListener {
                viewModel.upsertRecipeToFavorite(data)
            }
        }
    }
}
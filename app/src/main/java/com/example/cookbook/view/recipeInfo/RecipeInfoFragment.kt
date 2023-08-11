package com.example.cookbook.view.recipeInfo

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentRecipeInfoBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.utils.ID
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.recipeInfo.adapters.RecipeInformationPageAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeInfoFragment :
    BaseFragment<AppState, RecipeInformation, FragmentRecipeInfoBinding>(
        FragmentRecipeInfoBinding::inflate
    ) {

    private val viewModel: RecipeInfoViewModel by viewModel()

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

    override fun setupData(data: RecipeInformation) {
        with(binding) {
            chDairyFree.isChecked = data.dairyFree
            chGlutenFree.isChecked = data.glutenFree
            chVegan.isChecked = data.vegan
            chVegetarian.isChecked = data.vegetarian
            chVeryHealthy.isChecked = data.veryHealthy

            iconDairyFree.visibility = if (data.dairyFree) {View.VISIBLE} else {View.GONE}
            iconGlutenFree.visibility = if (data.glutenFree) {View.VISIBLE} else {View.GONE}
            iconVegan.visibility = if (data.vegan) {View.VISIBLE} else {View.GONE}
            iconVegetarian.visibility = if (data.vegetarian) {View.VISIBLE} else {View.GONE}
            iconHealthyFood.visibility = if (data.veryHealthy) {View.VISIBLE} else {View.GONE}

            tvRecipeInfoTitle.text = data.title
            ivRecipeInfoImage.load(data.image) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16f))
            }
//            tvRecipeInfoCalories.text = "${data.calories?.amount} ${data.calories?.unit}"
            chCalories.text = "${data.calories?.amount} ${data.calories?.unit}"
            chProtein.text = "${resources.getString(R.string.protein)} - ${data.protein?.amount}${data.protein?.unit}"
            chFat.text = "${resources.getString(R.string.fat)} - ${data.fat?.amount}${data.fat?.unit}"
            chCarb.text = "${resources.getString(R.string.carb)} - ${data.carbohydrates?.amount}${data.carbohydrates?.unit}"
            /*tvRecipeInfoProtein.text =
                "${data.protein?.amount}${data.protein?.unit}"
            tvRecipeInfoFat.text = "${data.fat?.amount}${data.fat?.unit}"
            tvRecipeInfoCarbohydrates.text =
                "${data.carbohydrates?.amount}${data.carbohydrates?.unit}"*/

            viewPager.adapter = RecipeInformationPageAdapter(data, requireActivity())
            viewPager.isUserInputEnabled = false
            TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = "Ingredient"
                    else -> tab.text = "Preparation"
                }
            }.attach()
        }
    }

    override fun showErrorDialog(message: String?) {

    }
}
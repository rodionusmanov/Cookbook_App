package com.example.cookbook.view.recipeInfo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RecipeInfoFragment :
    BaseFragment<AppState, RecipeInformation, FragmentRecipeInfoBinding>(
        FragmentRecipeInfoBinding::inflate
    ) {

    private val viewModel: RecipeInfoViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt(ID)

        id?.let { viewModel.recipeInfoRequest(it) }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.stateFlow.collect {
                    renderData(it)
                }
            }
        }
    }

    override fun setupData(data: RecipeInformation) {

        viewModel.setIngredients(data.extendedIngredients)
        viewModel.setInstructions(data.analyzedInstructions)
        viewModel.checkRecipeExistenceInDatabase(data.id)

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

            checkAndSetFavoriteChip(data)
        }
    }

    private fun checkAndSetFavoriteChip(data: RecipeInformation) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.recipeExistenceInDatabase.collect {
                    if (it) {
                        setChipAsDelete(data)
                    } else {
                        setChipAsAdd(data)
                    }
                }
            }
        }
    }

    private fun setChipAsAdd(data: RecipeInformation) {
        with(binding.chAddToFavorite) {
            isChecked = false
            text = resources.getString(R.string.add_to_favorite)
            setOnClickListener {
                viewModel.upsertRecipeToFavorite(data)
                setChipAsDelete(data)
            }
        }
    }

    private fun setChipAsDelete(data: RecipeInformation) {
        with(binding.chAddToFavorite) {
            isChecked = true
            text = resources.getString(R.string.delete_from_favorite)
            setOnClickListener {
                viewModel.deleteRecipeFromFavorite(data.id)
                setChipAsAdd(data)
            }
        }
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance(bundle: Bundle? = null) = RecipeInfoFragment().apply {
            arguments = bundle
        }
    }
}
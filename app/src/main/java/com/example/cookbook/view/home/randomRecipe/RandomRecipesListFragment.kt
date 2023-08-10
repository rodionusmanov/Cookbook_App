package com.example.cookbook.view.home.randomRecipe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.cookbook.stacklayoutmanager.StackLayoutManager
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentRandomRecipeListBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.utils.ID
import com.example.cookbook.view.base.BaseFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RandomRecipesListFragment :
    BaseFragment<AppState, List<RandomRecipeData>, FragmentRandomRecipeListBinding>(
        FragmentRandomRecipeListBinding::inflate
    ) {

    private lateinit var model: RandomRecipeListViewModel

    private val adapter: RandomRecipeListAdapter by lazy { RandomRecipeListAdapter() }

    companion object {
        fun newInstance(): RandomRecipesListFragment {
            return RandomRecipesListFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViewModel()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        val viewModel by viewModel<RandomRecipeListViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
        model.getRandomRecipes()
    }

    override fun setupData(data: List<RandomRecipeData>) {
        adapter.setData(data)
        val layoutManager = StackLayoutManager()
        binding.randomRecipesRecyclerView.adapter = adapter
        binding.randomRecipesRecyclerView.layoutManager = layoutManager

        initFavoritesListeners()

        adapter.listener = { recipe ->
            openRecipeInfoFragment(recipe.id)
        }
    }

    private fun openRecipeInfoFragment(recipeId: Int) {
        findNavController().navigate(
            R.id.action_navigation_home_to_recipeInfoFragment,
            Bundle().apply {
                putInt(ID, recipeId)
            })
    }

    private fun initFavoritesListeners() {
        adapter.listenerOnSaveRecipe = { recipe ->
            model.insertNewRecipeToDataBase(recipe)
        }

        adapter.listenerOnRemoveRecipe = { recipe ->
            model.deleteRecipeFromData(recipe.id)
        }
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
    }
}
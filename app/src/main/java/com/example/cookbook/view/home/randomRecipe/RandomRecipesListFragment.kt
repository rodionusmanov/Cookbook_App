package com.example.cookbook.view.home.randomRecipe

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cookbook.stacklayoutmanager.StackLayoutManager
import com.example.cookbook.databinding.FragmentRandomRecipeListBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.mainActivity.MainActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RandomRecipesListFragment :
    BaseFragment<AppState, List<RandomRecipeData>, FragmentRandomRecipeListBinding>(
        FragmentRandomRecipeListBinding::inflate
    ) {

    private val model: RandomRecipeListViewModel by viewModel()

    private val adapter: RandomRecipeListAdapter by lazy { RandomRecipeListAdapter(model) }
    private var navigationManager: NavigationManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    companion object {
        fun newInstance(): RandomRecipesListFragment {
            return RandomRecipesListFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        model.getRandomRecipes()
    }


    private fun initViewModel() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.stateFlow.collect {
                        renderData(it) }
                }
                launch {
                    model.recipeExistenceInDatabase.collect { result ->
                        result?.let { (id, exists) ->
                            adapter.updateRecipeExistence(id, exists)
                        }
                    }
                }
            }
        }
    }

    override fun setupData(data: List<RandomRecipeData>) {
        adapter.setData(data)
        val layoutManager = StackLayoutManager()
        binding.randomRecipesRecyclerView.adapter = adapter
        binding.randomRecipesRecyclerView.layoutManager = layoutManager
        adapter.listener = { recipe ->
            openRecipeInfoFragment(recipe.id)
        }
    }

    private fun openRecipeInfoFragment(recipeId: Int) {
        navigationManager?.openRecipeInfoFragment(recipeId)
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
    }
}
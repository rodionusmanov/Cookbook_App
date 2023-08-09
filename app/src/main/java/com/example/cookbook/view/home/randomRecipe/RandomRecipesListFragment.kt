package com.example.cookbook.view.home.randomRecipe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.cookbook.stacklayoutmanager.StackLayoutManager
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentRandomRecipeListBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.utils.ID
import com.example.cookbook.utils.parcelableArrayList
import com.example.cookbook.view.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RandomRecipesListFragment :
    BaseFragment<AppState, List<BaseRecipeData>, FragmentRandomRecipeListBinding>(
        FragmentRandomRecipeListBinding::inflate
    ) {

    private lateinit var model: RandomRecipeListViewModel

    private val adapter: RandomRecipeListAdapter by lazy { RandomRecipeListAdapter() }

    companion object {
        private const val RANDOM_RECIPE_LIST_KEY = "RandomRecipesListsKey"

        fun newInstance(randomData: List<BaseRecipeData>): RandomRecipesListFragment {
            return RandomRecipesListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(RANDOM_RECIPE_LIST_KEY, ArrayList(randomData))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.parcelableArrayList<RandomRecipeData>(RANDOM_RECIPE_LIST_KEY)
            ?.let { randomData ->
                setupData(randomData)
            }
        initViewModel()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViewModel() {
        val viewModel by viewModel<RandomRecipeListViewModel>()
        model = viewModel
    }

    override fun setupData(data: List<BaseRecipeData>) {
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
            R.id.action_navigation_search_recipe_to_recipeInfoFragment,
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
package com.example.cookbook.view.randomRecipe

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
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.utils.ID
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.searchRecipe.ISaveRecipe
import com.example.cookbook.viewModel.randomRecipeList.RandomRecipeListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RandomRecipesListFragment :
    BaseFragment<AppState, List<RandomRecipeData>, FragmentRandomRecipeListBinding>(
        FragmentRandomRecipeListBinding::inflate
    ) {

    private lateinit var model: RandomRecipeListViewModel

    private val adapter: RandomRecipeListAdapter by lazy { RandomRecipeListAdapter() }

    private lateinit var favoriteRecipes: List<SearchRecipeData>

    companion object {
        private const val RANDOM_RECIPE_LISTS_KEY = "RandomRecipesListsKey"

        fun newInstance(randomData: List<BaseRecipeData>): RandomRecipesListFragment {
            return RandomRecipesListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(RANDOM_RECIPE_LISTS_KEY, ArrayList(randomData))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getParcelableArrayList<RandomRecipeData>(RANDOM_RECIPE_LISTS_KEY)
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

    override fun setupData(data: List<RandomRecipeData>) {
        adapter.setData(data)
        val layoutManager = StackLayoutManager()
        binding.randomRecipesRecyclerView.adapter = adapter
        binding.randomRecipesRecyclerView.layoutManager = layoutManager

        adapter.listener = {
            findNavController().navigate(
                R.id.action_navigation_search_recipe_to_recipeInfoFragment,
                Bundle().apply { putInt(ID, it.id) })
        }
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Error {$message}", Toast.LENGTH_LONG).show()
    }


    private val callbackSaveItem = object : ISaveRecipe {
        override fun saveRecipe(recipe: SearchRecipeData) {
            model.insertNewRecipeToDataBase(recipe)
        }
    }
}
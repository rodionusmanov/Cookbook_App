package com.example.cookbook.view.home.randomRecipe

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cookbook.stacklayoutmanager.StackLayoutManager
import com.example.cookbook.databinding.FragmentRandomRecipeListBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.utils.navigation.NavigationUtils
import com.example.cookbook.utils.navigation.OnFragmentSwitchListener
import com.example.cookbook.view.base.BaseFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RandomRecipesListFragment :
    BaseFragment<AppState, List<RandomRecipeData>, FragmentRandomRecipeListBinding>(
        FragmentRandomRecipeListBinding::inflate
    ) {

    private lateinit var model: RandomRecipeListViewModel

    private val adapter: RandomRecipeListAdapter by lazy { RandomRecipeListAdapter() }

    private var listener: OnFragmentSwitchListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentSwitchListener){
            listener = context
        } else {
            throw RuntimeException("$context don't implement OnFragmentSwitchedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        fun newInstance(): RandomRecipesListFragment {
            return RandomRecipesListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        model.getRandomRecipes()
    }


    private fun initViewModel() {
        val viewModel by viewModel<RandomRecipeListViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
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
        val listener = activity as? OnFragmentSwitchListener
            ?: throw RuntimeException("Activity don't implement OnFragmentSwitchedListener")
        NavigationUtils.openRecipeInfoFragment(
            requireActivity().supportFragmentManager,
            listener = listener,
            recipeId
        )
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
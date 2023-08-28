package com.example.cookbook.view.favorite

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.databinding.FragmentFavoriteBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.utils.convertRecipeInfoEntityToList
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.mainActivity.MainActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoriteFragment : BaseFragment<AppState, List<RecipeInformation>, FragmentFavoriteBinding>(
    FragmentFavoriteBinding::inflate
) {

    private val viewModel: FavoriteRecipesViewModel by activityViewModel()
    private val adapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    private var navigationManager: NavigationManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.getRecipesFromDatabase()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect {
                    it.collect { entityData ->
                        val data = convertRecipeInfoEntityToList(entityData)
                        renderData(AppState.Success<List<RecipeInformation>>(data))
                    }
                }
            }
        }
    }

    override fun showErrorDialog(message: String?) {
//        TODO("Not yet implemented")
    }


    override fun setupData(data: List<RecipeInformation>) {
        adapter.setData(data)

        adapter.listener = { recipe ->
            openRecipeInfoFromDatabaseFragment(recipe.id)
        }

        binding.favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.favoriteRecipesRecyclerView.adapter = adapter
    }

    private fun openRecipeInfoFromDatabaseFragment(recipeId: Int) {
        navigationManager?.openRecipeInfoFromDatabaseFragment(recipeId)
    }

    companion object {
        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }
}
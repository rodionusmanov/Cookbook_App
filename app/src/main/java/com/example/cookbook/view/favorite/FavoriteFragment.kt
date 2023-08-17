package com.example.cookbook.view.favorite

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.databinding.FragmentFavoriteBinding
import com.example.cookbook.model.domain.RecipeInformation
import com.example.cookbook.utils.navigation.NavigationManager
import com.example.cookbook.view.mainActivity.MainActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    final val RECIPE_KEY = "recipe key"

    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding
        get() {
            return _binding!!
        }

    private lateinit var model: FavoriteRecipesViewModel
    private val adapter: FavoriteRecipesAdapter by lazy {
        FavoriteRecipesAdapter(
//            callbackDeleteRecipe
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationManager = (context as MainActivity).provideNavigationManager()
    }

    private var navigationManager: NavigationManager? = null

    private lateinit var favoriteRecipes: List<RecipeInformation>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        initViewModel()
        return binding.root
    }

    private fun initViewModel() {
        val viewModel by viewModel<FavoriteRecipesViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.getAllLocalRecipes().observe(viewLifecycleOwner) {
                    favoriteRecipes = it
                    setupData(favoriteRecipes)
                }
            }
        }
    }

    private fun setupData(favoriteRecipes: List<RecipeInformation>) {
        adapter.setData(favoriteRecipes)

        adapter.listener = { recipe ->
            openRecipeInfoFromDatabaseFragment(recipe.id)
        }

        binding.favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.favoriteRecipesRecyclerView.adapter = adapter
    }

    private val callbackDeleteRecipe = object : IDeleteRecipe {
        override fun deleteRecipe(id: Int) {
            AlertDialog.Builder(context)
                .setTitle("Deleting recipe")
                .setMessage("Do you really want to delete this recipe from your favorites?")
                .setPositiveButton("Yes") { dialog, _ ->
//                    model.deleteRecipeFromData(id)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
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
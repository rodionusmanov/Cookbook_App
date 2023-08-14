package com.example.cookbook.view.favorite

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.databinding.FragmentFavoriteBinding
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.view.search.searchResult.ISaveRecipe
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding
        get() {
            return _binding!!
        }

    private lateinit var model: FavoriteRecipesViewModel
    private val adapter: FavoriteRecipesAdapter by lazy {
        FavoriteRecipesAdapter(
            callbackDeleteRecipe
        )
    }

    private lateinit var favoriteRecipes: List<BaseRecipeData>
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
        model.getAllLocalRecipes().observe(viewLifecycleOwner) {
            favoriteRecipes = it
            setupData(favoriteRecipes)
        }
    }

    private fun setupData(favoriteRecipes: List<BaseRecipeData>) {
        adapter.setData(favoriteRecipes)
        binding.favoriteRecipesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.favoriteRecipesRecyclerView.adapter = adapter
    }

    private val callbackDeleteRecipe = object : IDeleteRecipe {
        override fun deleteRecipe(id: Int) {
            AlertDialog.Builder(context)
                .setTitle("Deleting recipe")
                .setMessage("Do you really want to delete this recipe from your favorites?")
                .setPositiveButton("Yes") { dialog, _ ->
                    model.deleteRecipeFromData(id)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("@@@", "FavoriteFragment is now resumed")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d("@@@", "FavoriteFragment is now hidden: $hidden")
    }
}
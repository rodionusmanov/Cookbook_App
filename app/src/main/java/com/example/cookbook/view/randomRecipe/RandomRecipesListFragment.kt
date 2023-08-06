package com.example.cookbook.view.randomRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cookbook.stacklayoutmanager.StackLayoutManager
import com.example.cookbook.databinding.FragmentRandomRecipeListBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.RandomRecipeData
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.searchRecipe.ISaveRecipe
import com.example.cookbook.viewModel.randomRecipeList.RandomRecipeListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RandomRecipesListFragment : BaseFragment<AppState>() {

    private var _binding: FragmentRandomRecipeListBinding? = null
    private val binding: FragmentRandomRecipeListBinding get() = _binding!!

    private lateinit var model: RandomRecipeListViewModel

    private val adapter: RandomRecipeListAdapter by lazy { RandomRecipeListAdapter(callbackSaveItem) }

    private lateinit var favoriteRecipes: List<SearchRecipeData>

    companion object {
        private const val RANDOM_RECIPE_LISTS_KEY = "RandomRecipesListsKey"

        fun newInstance(randomData: List<RandomRecipeData>): RandomRecipesListFragment {
            return RandomRecipesListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(RANDOM_RECIPE_LISTS_KEY, ArrayList(randomData))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomRecipeListBinding.inflate(inflater, container, false)

        arguments?.getParcelableArrayList<RandomRecipeData>(RANDOM_RECIPE_LISTS_KEY)
            ?.let { randomData ->
                setupData(randomData)
            }

        initViewModel()

        return binding.root
    }

    private fun initViewModel() {
        val viewModel by viewModel<RandomRecipeListViewModel>()
        model = viewModel
    }

    override fun setupData(data: Any?) {
        val randomRecipeData = data as List<RandomRecipeData>
        adapter.setData(randomRecipeData)
        val layoutManager = StackLayoutManager()
        binding.randomRecipesRecyclerView.adapter = adapter
        binding.randomRecipesRecyclerView.layoutManager = layoutManager
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Ошибка {$message}", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val callbackSaveItem = object : ISaveRecipe {
        override fun saveRecipe(recipe: SearchRecipeData) {
            model.insertNewRecipeToDataBase(recipe)
        }
    }

}
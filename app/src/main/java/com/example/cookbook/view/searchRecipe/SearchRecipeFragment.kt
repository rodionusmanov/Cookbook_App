package com.example.cookbook.view.searchRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cookbook.databinding.FragmentSearchRecipeBinding
import com.example.cookbook.domain.Recipe
import com.example.cookbook.model.AppState
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.viewModel.searchRecipe.SearchRecipeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchRecipeFragment : BaseFragment<AppState, Recipe>() {

    private var _binding: FragmentSearchRecipeBinding? = null
    private val binding: FragmentSearchRecipeBinding
        get() {
            return _binding!!
        }

    private val viewModel by viewModel<SearchRecipeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSearchRecipe.setOnClickListener {
            getBeefResponse()
        }
    }

    override fun showErrorDialog() {
        TODO("Not yet implemented")
    }


    private fun getBeefResponse() {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.searchRecipeRequest("beef mushroom")
            withContext(Dispatchers.Main){
                viewModel.searchRecipeLiveData.observe(viewLifecycleOwner) {
                    renderData(it)
                }
            }
        }
    }

    override fun setupData(data: List<Recipe>) {
        with(binding){
            val firstTitle = data.first().recipeName
            tvSearchRecipe.text = firstTitle
        }
    }
}
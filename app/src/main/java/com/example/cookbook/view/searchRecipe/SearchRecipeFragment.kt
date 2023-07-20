package com.example.cookbook.view.searchRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cookbook.databinding.FragmentSearchRecipeBinding
import com.example.cookbook.viewModel.searchRecipe.SearchRecipeFragmentAppState
import com.example.cookbook.viewModel.searchRecipe.SearchRecipeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchRecipeFragment : Fragment() {

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

    private fun renderData(it: SearchRecipeFragmentAppState) {
        when (it) {
            is SearchRecipeFragmentAppState.Success -> {
                with(binding){
                    val firstTitle = it.recipeList.first().recipeName
                    tvSearchRecipe.text = firstTitle
                }
            }
            SearchRecipeFragmentAppState.Loading -> {}
            else -> {}
        }
    }
}
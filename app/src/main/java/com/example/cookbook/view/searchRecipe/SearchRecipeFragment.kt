package com.example.cookbook.view.searchRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cookbook.databinding.FragmentSearchRecipeBinding
import com.example.cookbook.domain.Recipe
import com.example.cookbook.model.AppState
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.viewModel.searchRecipe.SearchRecipeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchRecipeFragment : BaseFragment<AppState, Recipe>() {

    private var _binding: FragmentSearchRecipeBinding? = null
    private val binding: FragmentSearchRecipeBinding
        get() {
            return _binding!!
        }

    private lateinit var model: SearchRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchRecipeBinding.inflate(inflater, container, false)

        initViewModel()

        return binding.root
    }

    private fun initViewModel() {
        val viewModel by viewModel<SearchRecipeViewModel>()
        model = viewModel
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                model.stateFlow.collect{ renderData(it)}
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSearchRecipe.setOnClickListener {
            getBeefResponse()
        }
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Ошибка {$message}", Toast.LENGTH_LONG).show()
    }

    private fun getBeefResponse() {
        model.searchRecipeRequest("beef mushroom")
    }

    override fun setupData(data: List<Recipe>) {
        with(binding){
            val firstTitle = data.first().recipeName
            tvSearchRecipe.text = firstTitle
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
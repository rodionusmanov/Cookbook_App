package com.example.cookbook.view.searchRecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val adapter: SearchRecipeAdapter by lazy { SearchRecipeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchRecipeBinding.inflate(inflater, container, false)

        initViewModel()
        setupSearchView()

        return binding.root
    }

    private fun setupSearchView() {

        binding.searchView.setOnQueryTextListener(
            object: OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let{
                        model.searchRecipeRequest(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }

            }
        )
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

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, "Ошибка {$message}", Toast.LENGTH_LONG).show()
    }
    
    override fun setupData(data: List<Recipe>) {
        adapter.setData(data)
        binding.resultRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.resultRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
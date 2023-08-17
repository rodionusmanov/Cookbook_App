package com.example.cookbook.view.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentSearchBinding
import com.example.cookbook.model.AppState
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.utils.ID
import com.example.cookbook.view.base.BaseFragment
import com.example.cookbook.view.recipeInfo.RecipeInfoFragment
import com.example.cookbook.view.search.searchResult.SearchResultAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : BaseFragment<AppState, List<BaseRecipeData>, FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    private val model: SearchViewModel by activityViewModel()

    private val adapter: SearchResultAdapter by lazy { SearchResultAdapter() }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle? = null) = SearchFragment().apply {
            arguments = bundle
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resultRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.resultRecyclerView.adapter = adapter

        initViewModel()
        setupSearchView()


    }

    private fun initViewModel() {
        arguments?.getString("search")?.let {
            binding.searchView.setQuery(it, false)
            model.searchRecipeRequest(
                it,
                ""
            )
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.stateFlow.collect { renderData(it) }
            }
        }
    }

    private fun setupSearchView() {

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        model.searchRecipeRequest(it, "")
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            }
        )
    }

    override fun setupData(data: List<BaseRecipeData>) {
        adapter.submitList(data)

        adapter.listener = { recipe ->
            openRecipeInfoFragment(recipe.id)
        }
    }

    private fun openRecipeInfoFragment(recipeId: Int) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_container, RecipeInfoFragment.newInstance(Bundle().apply {
                putInt(ID, recipeId)
            }))
            .addToBackStack("info")
            .commit()
    }

    override fun showErrorDialog(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
package com.example.cookbook.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cookbook.databinding.ItemSearchResultBinding
import com.example.cookbook.model.domain.BaseRecipeData
import kotlinx.coroutines.launch

class SearchResultAdapter(
    private val viewModel: SearchViewModel,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val lifecycle: Lifecycle
) :
    ListAdapter<BaseRecipeData, SearchResultAdapter.RecyclerItemViewHolder>(SearchCallback()) {

    var listener: ((BaseRecipeData) -> Unit)? = null

    private val recipeExistenceMap = mutableMapOf<Int, Boolean>()

    init {
        observeRecipeExistence()
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: BaseRecipeData) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                ItemSearchResultBinding.bind(itemView).apply {
                    setTextAndImage(data)
                    setCheckBox(data.id)
                    setOnClickListener(data)
                }
            }
        }
    }

    class SearchCallback : DiffUtil.ItemCallback<BaseRecipeData>() {
        override fun areItemsTheSame(
            oldItem: BaseRecipeData,
            newItem: BaseRecipeData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BaseRecipeData,
            newItem: BaseRecipeData
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private fun ItemSearchResultBinding.setOnClickListener(data: BaseRecipeData) {
        root.setOnClickListener { listener?.invoke(data) }
    }

    private fun ItemSearchResultBinding.setTextAndImage(data: BaseRecipeData) {
        tvSearchRecipe.text = data.title

        ivSearchRecipe.load(data.image) {
            crossfade(500)
            scale(Scale.FILL)
            listener(
                onStart = {
                    progressBar.visibility = View.VISIBLE
                },
                onSuccess = {_,_ ->
                    progressBar.visibility = View.GONE
                },
                onError = {_,_ ->
                    progressBar.visibility = View.GONE
                }
            )
        }
    }

    private fun ItemSearchResultBinding.setCheckBox(id: Int) {
        ivAddFavorite.isChecked = recipeExistenceMap[id] ?: false
        ivAddFavorite.isClickable = false
        ivAddFavorite.isFocusable = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun updateRecipeExistence(id: Int, exists: Boolean) {
        recipeExistenceMap[id] = exists
        val position = currentList.indexOfFirst { it.id == id }
        if (position != -1) {
            notifyItemChanged(position)
        }
    }

    private fun observeRecipeExistence() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipeExistenceInDatabase.collect { result ->
                    result?.let { (id, exists) ->
                        updateRecipeExistence(id, exists)
                    }
                }
            }
        }
    }

    fun setData(data: List<BaseRecipeData>) {
        submitList(data)
        viewModel.setRecipeIdsToWatch(data.map {it.id})
    }
}
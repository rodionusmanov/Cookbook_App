package com.example.cookbook.view.home.randomRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cookbook.R
import com.example.cookbook.databinding.ItemRandomRecipeRvBinding
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RandomRecipeData
import kotlinx.coroutines.launch

class RandomRecipeListAdapter(
    private val viewModel: CheckRecipeExistenceViewModelExistence,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val lifecycle: Lifecycle
) :
    RecyclerView.Adapter<RandomRecipeListAdapter.RecyclerItemViewHolder>() {

    private var data: List<RandomRecipeData> = arrayListOf()
    var listener: ((BaseRecipeData) -> Unit)? = null

    private val recipeExistenceMap = mutableMapOf<Int, Boolean>()

    init {
        observeRecipeExistence()
    }

    fun setData(data: List<RandomRecipeData>) {
        this.data = data
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: BaseRecipeData) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                ItemRandomRecipeRvBinding.bind(itemView).apply {
                    viewModel.observeRecipeExistenceInDatabase(data.id)
                    setTextAndImage(data)
                    setOnClickListener(data)
                    setCheckBox(data)
                }
            }
        }

        private fun ItemRandomRecipeRvBinding.setCheckBox(data: BaseRecipeData) {
            cbAddFavorite.isChecked = recipeExistenceMap[data.id] ?: false
            cbAddFavorite.isClickable = false
            cbAddFavorite.isFocusable = false
        }

        private fun ItemRandomRecipeRvBinding.setOnClickListener(data: BaseRecipeData) {
            root.setOnClickListener { listener?.invoke(data) }
        }

        private fun ItemRandomRecipeRvBinding.setTextAndImage(data: BaseRecipeData) {
            randomRecipeTitle.text = data.title

            val cookingTime = data.readyInMinutes.toString() + " min"
            tvCookingTime.text = cookingTime

            randomRecipeImage.load(data.image) {
                crossfade(500)
                scale(Scale.FILL)
                placeholder(R.drawable.icon_search)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding = ItemRandomRecipeRvBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerItemViewHolder(binding.root)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    private fun updateRecipeExistence(id: Int, exists: Boolean) {
        recipeExistenceMap[id] = exists
        val position = data.indexOfFirst { it.id == id }
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
}
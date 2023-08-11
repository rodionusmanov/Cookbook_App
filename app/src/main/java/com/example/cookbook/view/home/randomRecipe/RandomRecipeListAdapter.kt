package com.example.cookbook.view.home.randomRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cookbook.R
import com.example.cookbook.databinding.ItemRandomRecipeRvBinding
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.domain.RandomRecipeData

class RandomRecipeListAdapter :
    RecyclerView.Adapter<RandomRecipeListAdapter.RecyclerItemViewHolder>() {

    private var data: List<RandomRecipeData> = arrayListOf()
    var listener: ((BaseRecipeData) -> Unit)? = null
    var listenerOnSaveRecipe: ((BaseRecipeData) -> Unit)? = null
    var listenerOnRemoveRecipe: ((BaseRecipeData) -> Unit)? = null

    fun setData(data: List<RandomRecipeData>) {
        this.data = data
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: BaseRecipeData) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                ItemRandomRecipeRvBinding.bind(itemView).apply {
                    setTextAndImage(data)
                    setOnClickListener(data)
                    setCheckBox(data)
                }
            }
        }

        private fun ItemRandomRecipeRvBinding.setCheckBox(data: BaseRecipeData) {
            cbAddFavorite.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    listenerOnSaveRecipe?.invoke(data)
                } else {
                    listenerOnRemoveRecipe?.invoke(data)
                }
            }
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
}
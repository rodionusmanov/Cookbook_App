package com.example.cookbook.view.searchRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cookbook.R
import com.example.cookbook.databinding.ItemSearchResultBinding
import com.example.cookbook.model.domain.SearchRecipeData

class SearchRecipeAdapter() :
    RecyclerView.Adapter<SearchRecipeAdapter.RecyclerItemViewHolder>() {

    private var data: List<SearchRecipeData> = arrayListOf()
    var listener: ((SearchRecipeData) -> Unit)? = null
    var listenerOnSaveRecipe: ((SearchRecipeData) -> Unit)? = null
    var listenerOnRemoveRecipe: ((SearchRecipeData) -> Unit)? = null

    fun setData(data: List<SearchRecipeData>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: SearchRecipeData) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                ItemSearchResultBinding.bind(itemView).apply {
                    setTextAndImage(data)
                    setCheckBox(data)
                    setOnClickListener(data)
                }
            }
        }

        private fun ItemSearchResultBinding.setOnClickListener(data: SearchRecipeData) {
            root.setOnClickListener { listener?.invoke(data) }
        }

        private fun ItemSearchResultBinding.setTextAndImage(data: SearchRecipeData) {
            tvSearchRecipe.text = data.title
            ivSearchRecipe.load(data.image) {
                crossfade(500)
                scale(Scale.FILL)
                placeholder(R.drawable.icon_search)
            }
        }

        private fun ItemSearchResultBinding.setCheckBox(data: SearchRecipeData) {
            ivAddFavorite.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    listenerOnSaveRecipe?.invoke(data)
                } else {
                    listenerOnRemoveRecipe?.invoke(data)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerItemViewHolder(binding.root)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
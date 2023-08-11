package com.example.cookbook.view.search.searchResult

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cookbook.R
import com.example.cookbook.databinding.ItemSearchResultBinding
import com.example.cookbook.model.domain.BaseRecipeData

class SearchResultAdapter :
    RecyclerView.Adapter<SearchResultAdapter.RecyclerItemViewHolder>() {

    private var data: List<BaseRecipeData> = arrayListOf()
    var listener: ((BaseRecipeData) -> Unit)? = null
    var listenerOnSaveRecipe: ((BaseRecipeData) -> Unit)? = null
    var listenerOnRemoveRecipe: ((BaseRecipeData) -> Unit)? = null

    fun setData(data: List<BaseRecipeData>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: BaseRecipeData) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                ItemSearchResultBinding.bind(itemView).apply {
                    setTextAndImage(data)
                    setCheckBox(data)
                    setOnClickListener(data)
                }
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
                placeholder(R.drawable.icon_search)
            }
        }

        private fun ItemSearchResultBinding.setCheckBox(data: BaseRecipeData) {
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
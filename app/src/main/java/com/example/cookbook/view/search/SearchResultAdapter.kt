package com.example.cookbook.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cookbook.R
import com.example.cookbook.databinding.ItemSearchResultBinding
import com.example.cookbook.model.domain.BaseRecipeData
import com.example.cookbook.model.interactor.RandomRecipeListInteractor
import com.example.cookbook.view.favorite.FavoriteRecipesViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchResultAdapter :
    ListAdapter<BaseRecipeData, SearchResultAdapter.RecyclerItemViewHolder>(SearchCallback()) {

    var listener: ((BaseRecipeData) -> Unit)? = null

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
            placeholder(R.drawable.icon_search)
        }
    }

    private fun ItemSearchResultBinding.setCheckBox(id: Int) {
        ivAddFavorite.isChecked = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
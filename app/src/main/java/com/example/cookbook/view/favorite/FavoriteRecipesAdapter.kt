package com.example.cookbook.view.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cookbook.R
import com.example.cookbook.databinding.ItemSearchResultBinding
import com.example.cookbook.model.domain.RecipeInformation

class FavoriteRecipesAdapter :
    RecyclerView.Adapter<FavoriteRecipesAdapter.RecyclerItemViewHolder>() {

    private var data: List<RecipeInformation> = arrayListOf()
    var listener: ((RecipeInformation) -> Unit)? = null
    var addRecipeListener: ((RecipeInformation) -> Unit)? = null
    var deleteRecipeListener: ((Int) -> Unit)? = null
    fun setData(data: List<RecipeInformation>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: RecipeInformation) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                ItemSearchResultBinding.bind(itemView).apply {
                    ivAddFavorite.isChecked = true
                    tvSearchRecipe.text = data.title
                    ivSearchRecipe.load(data.image) {
                        crossfade(500)
                        scale(Scale.FILL)
                        placeholder(R.drawable.icon_search)
                    }
                    setOnClickListener(data)
                    ivAddFavorite.setOnCheckedChangeListener { compoundButton, isFavorite ->
                        if (isFavorite) {
                            deleteRecipeListener?.invoke(data.id)
                        } else {
                            addRecipeListener?.invoke(data)
                        }
                        compoundButton.isChecked = !ivAddFavorite.isChecked
                    }
                }
            }
        }

        private fun ItemSearchResultBinding.setOnClickListener(data: RecipeInformation) {
            root.setOnClickListener { listener?.invoke(data) }
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
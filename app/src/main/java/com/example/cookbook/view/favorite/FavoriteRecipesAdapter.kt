package com.example.cookbook.view.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.cookbook.R
import com.example.cookbook.databinding.ItemSearchResultBinding
import com.example.cookbook.model.domain.SearchRecipeData
import com.example.cookbook.view.searchRecipe.ISaveRecipe

class FavoriteRecipesAdapter(val callbackSaveRecipe: ISaveRecipe) : RecyclerView.Adapter<FavoriteRecipesAdapter.RecyclerItemViewHolder>() {

    private var data: List<SearchRecipeData> = arrayListOf()

    fun setData(data: List<SearchRecipeData>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: SearchRecipeData) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                ItemSearchResultBinding.bind(itemView).apply {
                    tvSearchRecipe.text = data.title
                    ivSearchRecipe.load(data.image) {
                        crossfade(500)
                        scale(Scale.FILL)
                        placeholder(R.drawable.icon_search)
                    }
                    ivAddFavorite.setOnClickListener {
                        callbackSaveRecipe.saveRecipe(data)
                        ivAddFavorite.setImageResource(R.drawable.icon_favorite_solid)
                        ivAddFavorite.setBackgroundResource(R.color.orange_dark)

                        /*ivAddFavorite.setImageResource(R.drawable.icon_favorite)
                        ivAddFavorite.setBackgroundResource(R.color.orange_light)
                        itemView.setBackgroundResource(R.color.white)*/
                    }
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
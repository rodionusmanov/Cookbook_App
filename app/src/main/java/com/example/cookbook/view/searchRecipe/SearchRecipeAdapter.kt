package com.example.cookbook.view.searchRecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.R
import com.example.cookbook.domain.Recipe

class SearchRecipeAdapter : RecyclerView.Adapter<SearchRecipeAdapter.RecyclerItemViewHolder>() {

    private var data: List<Recipe> = arrayListOf()

    fun setData(data: List<Recipe>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: Recipe) {
            if (layoutPosition != RecyclerView.NO_POSITION){
                itemView.findViewById<TextView>(R.id.tv_search_recipe).text = data.recipeName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_result, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
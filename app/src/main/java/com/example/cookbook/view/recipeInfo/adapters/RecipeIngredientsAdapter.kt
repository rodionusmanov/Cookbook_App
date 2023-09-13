package com.example.cookbook.view.recipeInfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cookbook.R
import com.example.cookbook.databinding.ItemIngredientsBinding
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient

class RecipeIngredientsAdapter(
    private val context: Context?
) :
    ListAdapter<ExtendedIngredient, RecipeIngredientsAdapter.RecipeIngredientsViewHolder>(
        UniversalCallback()
) {

    inner class RecipeIngredientsViewHolder(private val binding: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ExtendedIngredient) {
            with(binding) {
                tvIngredientName.text =
                    context?.getString(
                        R.string.ingredient_format, data.originalName,
                        formatAmount(data.measures.metric.amount),
                        data.measures.metric.unitLong)
                ivIngredientImage.load(data.image) {
                    crossfade(true)
                }
            }
        }
    }

    private fun formatAmount(amount:Double): String {
        return if(amount % 1 == 0.0) {
            String.format("%.0f", amount)
        } else {
            amount.toString()
        }
    }

    class UniversalCallback : DiffUtil.ItemCallback<ExtendedIngredient>() {
        override fun areItemsTheSame(oldItem: ExtendedIngredient, newItem: ExtendedIngredient): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExtendedIngredient, newItem: ExtendedIngredient): Boolean {
            return oldItem.originalName == newItem.originalName && oldItem.image == newItem.image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientsViewHolder {
        val binding =
            ItemIngredientsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RecipeIngredientsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeIngredientsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
package com.example.cookbook.view.recipeInfo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cookbook.databinding.ItemIngredientBinding
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    private var data: List<ExtendedIngredient> = arrayListOf()

    fun setData(data: List<ExtendedIngredient>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class IngredientsViewHolder(private val binding: ItemIngredientBinding) :
        ViewHolder(binding.root) {
        fun bind(data: ExtendedIngredient) {
            with(binding) {
                tvIngredient.text =
                    "${data.originalName} ${data.measures.metric.amount} ${data.measures.metric.unitLong}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientsViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
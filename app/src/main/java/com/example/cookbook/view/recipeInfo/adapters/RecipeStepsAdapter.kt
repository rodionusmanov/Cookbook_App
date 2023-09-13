package com.example.cookbook.view.recipeInfo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbook.databinding.ItemStepsBinding
import com.example.cookbook.model.datasource.DTO.recipeInformation.Step

class RecipeStepsAdapter : ListAdapter<Step, RecipeStepsAdapter.RecipeStepsViewHolder>(
    UniversalCallback()
) {

        inner class RecipeStepsViewHolder(private val binding: ItemStepsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Step) {
                with(binding) {
                    stepNumberText.text = data.number.toString()
                    stepText.text = data.step
                }
            }
        }

        class UniversalCallback : DiffUtil.ItemCallback<Step>() {
            override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
                return oldItem.number == newItem.number && oldItem.step == newItem.step
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeStepsViewHolder {
            val binding =
                ItemStepsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return RecipeStepsViewHolder(binding)
        }

    override fun onBindViewHolder(holder: RecipeStepsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
package com.example.cookbook.view.recipeInfo.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.cookbook.databinding.ItemUniversalBinding
import com.example.cookbook.model.datasource.DTO.recipeInformation.Equipment
import com.example.cookbook.model.datasource.DTO.recipeInformation.ExtendedIngredient
import com.example.cookbook.model.datasource.DTO.recipeInformation.Ingredient
import com.example.cookbook.model.datasource.DTO.recipeInformation.Step
import com.example.cookbook.model.domain.UniversalItem

class UniversalAdapter :
    ListAdapter<UniversalItem, UniversalAdapter.UniversalViewHolder>(UniversalCallback()) {


    inner class UniversalViewHolder(private val binding: ItemUniversalBinding) :
        ViewHolder(binding.root) {
        fun bind(data: UniversalItem) {
            when (data) {
                is ExtendedIngredient -> {
                    with(binding) {
                        tvUniversalItem.text =
                            "${data.originalName} ${data.measures.metric.amount} ${data.measures.metric.unitLong}"
                        ivUniversalItem.isVisible = false
                    }
                }

                is Equipment -> {
                    with(binding) {
                        llUniversalItem.gravity = Gravity.CENTER_HORIZONTAL
                        tvUniversalItem.text = data.name
                        ivUniversalItem.isVisible = true
                        ivUniversalItem.load(data.image) {
                            crossfade(true)
                        }
                    }
                }

                is Ingredient -> {
                    with(binding) {
                        llUniversalItem.gravity = Gravity.CENTER_HORIZONTAL
                        tvUniversalItem.text = data.name
                        ivUniversalItem.isVisible = true
                        ivUniversalItem.load(data.image) {
                            crossfade(true)
                        }
                    }
                }

                is Step -> {
                    with(binding) {
                        tvUniversalItem.text = data.step
                        ivUniversalItem.isVisible = false
                    }
                }
            }
        }
    }

    class UniversalCallback : DiffUtil.ItemCallback<UniversalItem>() {
        override fun areItemsTheSame(oldItem: UniversalItem, newItem: UniversalItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UniversalItem, newItem: UniversalItem): Boolean {
            return true
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversalViewHolder {
        val binding =
            ItemUniversalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return UniversalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UniversalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
package com.example.cookbook.view.recipeInfo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.cookbook.databinding.ItemIngredientsAndEquipmentsBinding
import com.example.cookbook.model.datasource.DTO.recipeInformation.Equipment

class EquipmentsPreparationAdapter :
    RecyclerView.Adapter<EquipmentsPreparationAdapter.InstructionsViewHolder>() {

    private var data: List<Equipment> = arrayListOf()

    fun setData(data: List<Equipment>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class InstructionsViewHolder(private val binding: ItemIngredientsAndEquipmentsBinding) :
        ViewHolder(binding.root) {
        fun bind(data: Equipment) {
            with(binding) {
                tvTitlePreparationItem.text = data.name
                ivImagePreparationItem.load(data.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionsViewHolder {
        val binding =
            ItemIngredientsAndEquipmentsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return InstructionsViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: InstructionsViewHolder, position: Int) {
        holder.bind(data[position])
    }
}
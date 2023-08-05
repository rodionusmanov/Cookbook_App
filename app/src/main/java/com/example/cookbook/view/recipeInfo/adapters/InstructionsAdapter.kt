package com.example.cookbook.view.recipeInfo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cookbook.databinding.ItemAnalyzedInstructionsBinding
import com.example.cookbook.model.datasource.DTO.recipeInformation.Step

class InstructionsAdapter : RecyclerView.Adapter<InstructionsAdapter.InstructionsViewHolder>() {

    private var data: List<Step> = arrayListOf()

    fun setData(data: List<Step>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class InstructionsViewHolder(private val binding: ItemAnalyzedInstructionsBinding) :
        ViewHolder(binding.root) {
        fun bind(data: Step) {
            with(binding) {
                tvStep.text = data.step
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionsViewHolder {
        val binding =
            ItemAnalyzedInstructionsBinding.inflate(
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
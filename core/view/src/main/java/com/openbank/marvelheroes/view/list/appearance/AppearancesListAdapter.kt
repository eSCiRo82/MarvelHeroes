package com.openbank.marvelheroes.view.list.appearance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openbank.marvelheroes.view.databinding.ItemAppearanceBinding
import com.openbank.marvelheroes.view.databinding.ItemSectionBinding
import com.openbank.marvelheroes.common.adapter.BindViewHolder

class AppearancesListAdapter(
    private val onAppearanceClick: (String) -> Unit
) : RecyclerView.Adapter<BindViewHolder<*>>() {

    private val sectionType = 0
    private val appearanceType = 1

    private val items : MutableList<AppearancesListItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder<*> =
        when (viewType) {
            appearanceType -> {
                val binding = ItemAppearanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AppearanceViewHolder(binding, onAppearanceClick)
            }
            else -> {
                val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionViewHolder(binding)
            }
        }

    override fun onBindViewHolder(holder: BindViewHolder<*>, position: Int) =
        when (items[position]) {
            is AppearanceItem -> (holder as AppearanceViewHolder).bind(items[position] as AppearanceItem)
            else -> (holder as SectionViewHolder).bind(items[position] as SectionItem)
        }
    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is AppearanceItem -> appearanceType
            else -> sectionType
        }

    fun addAppearances(appearances: List<AppearanceItem>) {
        val count = items.size
        items.addAll(appearances)
        notifyItemRangeInserted(count, appearances.size)
    }

    fun addSection(section: SectionItem) {
        items.add(section)
        notifyItemInserted(items.size-1)
    }
}
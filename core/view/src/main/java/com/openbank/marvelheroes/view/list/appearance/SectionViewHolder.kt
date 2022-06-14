package com.openbank.marvelheroes.view.list.appearance

import com.openbank.marvelheroes.view.databinding.ItemSectionBinding
import com.openbank.marvelheroes.common.adapter.BindViewHolder

class SectionViewHolder(
    private val binding: ItemSectionBinding
) : BindViewHolder<SectionItem>(binding.root) {

    override fun bind(item: SectionItem) {
        binding.root.text = item.title
    }
}
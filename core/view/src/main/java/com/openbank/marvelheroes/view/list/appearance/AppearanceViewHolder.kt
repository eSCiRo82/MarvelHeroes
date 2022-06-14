package com.openbank.marvelheroes.view.list.appearance

import com.openbank.marvelheroes.view.databinding.ItemAppearanceBinding
import com.openbank.marvelheroes.common.adapter.BindViewHolder
import com.openbank.marvelheroes.view.listener.OnSafeClickListener

class AppearanceViewHolder(
    private val binding: ItemAppearanceBinding,
    private val onClick: (String) -> Unit
): BindViewHolder<AppearanceItem>(binding.root) {

    override fun bind(item: AppearanceItem) {
        with (binding) {
            root.text = item.name
            root.setOnClickListener(OnSafeClickListener(500) { onClick(item.url) })
        }
    }
}
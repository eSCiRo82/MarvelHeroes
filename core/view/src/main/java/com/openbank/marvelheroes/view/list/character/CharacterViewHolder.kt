package com.openbank.marvelheroes.view.list.character

import com.openbank.marvelheroes.common.adapter.BindViewHolder
import com.openbank.marvelheroes.view.listener.OnSafeClickListener
import com.openbank.marvelheroes.view.databinding.CardviewCharacterBinding

class CharacterViewHolder(
    private val binding: CardviewCharacterBinding,
    private val onClick: (Int) -> Unit
): BindViewHolder<CharacterItem>(binding.root) {

    override fun bind(item: CharacterItem) {
        with (binding) {
            characterImage.setImageBitmap(item.image)
            characterName.text = item.name
            root.setOnClickListener(OnSafeClickListener(500) { onClick(item.id) })
        }
    }
}
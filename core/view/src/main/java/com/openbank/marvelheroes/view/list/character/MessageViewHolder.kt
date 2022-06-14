package com.openbank.marvelheroes.view.list.character

import com.openbank.marvelheroes.common.adapter.BindViewHolder
import com.openbank.marvelheroes.view.databinding.CardviewMessageBinding

class MessageViewHolder(
    private val binding: CardviewMessageBinding
) : BindViewHolder<String>(binding.root) {

    override fun bind(item: String) {
        binding.loadingMessageName.text = item
    }
}
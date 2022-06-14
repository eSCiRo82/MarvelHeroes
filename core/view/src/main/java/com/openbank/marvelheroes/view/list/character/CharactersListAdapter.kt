package com.openbank.marvelheroes.view.list.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openbank.marvelheroes.common.adapter.BindViewHolder
import com.openbank.marvelheroes.view.databinding.CardviewCharacterBinding
import com.openbank.marvelheroes.view.databinding.CardviewMessageBinding

class CharactersListAdapter(
    private val onCharacterClick: (Int) -> Unit
): RecyclerView.Adapter<BindViewHolder<*>>() {

    private val characterType = 0
    private val messageType = 1

    private val items: MutableList<CharactersListItem> = mutableListOf()

    private var minimumItemHeight = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder<*> =
        when (viewType) {
            characterType -> {
                val binding = CardviewCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CharacterViewHolder(binding, onCharacterClick)
            }
            else -> {
                val binding = CardviewMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MessageViewHolder(binding)
            }
        }

    override fun onBindViewHolder(holder: BindViewHolder<*>, position: Int) =
        when (items[position]) {
            is CharacterItem -> {
                minimumItemHeight = maxOf(minimumItemHeight, holder.itemView.measuredHeight)
                (holder as CharacterViewHolder).bind(items[position] as CharacterItem)
            }
            is MessageItem -> {
                if (minimumItemHeight > 0) holder.itemView.minimumHeight = minimumItemHeight
                (holder as MessageViewHolder).bind((items[position] as MessageItem).message)
            }
            else -> (holder as MessageViewHolder).bind("")
        }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is CharacterItem -> characterType
            else -> messageType
        }

    fun addCharacters(characters: List<CharacterItem>) {
        val count = items.size
        items.addAll(characters)
        notifyItemRangeInserted(count, characters.size)
    }

    fun showMessage(message: String) {
        items.add(MessageItem(message))
        notifyItemInserted(items.size-1)
    }

    fun removeItemIf(position: Int, cond: (CharactersListItem?) -> Boolean) {
        if (position >= 0 && cond(items[position])) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeAll() {
        val count = items.size
        items.clear()
        notifyItemRangeRemoved(0, count)
    }
}
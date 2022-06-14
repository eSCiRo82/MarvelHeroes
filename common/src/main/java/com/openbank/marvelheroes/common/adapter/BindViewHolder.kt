package com.openbank.marvelheroes.common.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Base class for ViewHolder objects.
 *
 * The bind method must be implemented to set the data in the view.
 */
abstract class BindViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: T)
}
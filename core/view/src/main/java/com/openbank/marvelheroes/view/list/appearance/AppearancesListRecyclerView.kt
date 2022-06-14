package com.openbank.marvelheroes.view.list.appearance

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * List of appearances of a character. Two types of items can be contained in the list: an item
 * showing the name of the section and the list of titles of the elements.
 */
class AppearancesListRecyclerView(context: Context, attrs: AttributeSet? = null)
    : RecyclerView(context, attrs) {

    private var appearancesListAdapter : AppearancesListAdapter

    private val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    var onItemClick: ((String) -> Unit)? = null

    init {
        appearancesListAdapter = AppearancesListAdapter { url -> onItemClick?.invoke(url) }

        adapter = appearancesListAdapter
        layoutManager = linearLayoutManager
    }

    fun addSection(section: String, appearances: List<AppearanceItem>) {
        appearancesListAdapter.addSection(SectionItem(section))
        appearancesListAdapter.addAppearances(appearances)
    }
}
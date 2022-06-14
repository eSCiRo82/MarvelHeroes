package com.openbank.marvelheroes.view.list.character

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerOnScrollListener(
    private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private val visibleThreshold = 10
    private var loading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0) {
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (!loading) {
                if (visibleItemCount + firstVisibleItem >= totalItemCount &&
                    firstVisibleItem >= 0 &&
                    totalItemCount >= visibleThreshold) {
                    loading = true
                    recyclerView.post { limitReached() }
                }
            }
        }
    }

    abstract fun limitReached()

    fun loadOk() {
        loading = false
    }
}

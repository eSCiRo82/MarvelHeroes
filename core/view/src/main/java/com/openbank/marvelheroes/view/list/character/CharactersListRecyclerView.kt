package com.openbank.marvelheroes.view.list.character

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

/**
 * List of characters. This list is shown as a gallery with cards where the image and the name of
 * the each character are shown.
 */
class CharactersListRecyclerView(context: Context, attrs: AttributeSet? = null)
    : RecyclerView(context, attrs) {

    private var charactersListAdapter : CharactersListAdapter
    private var recyclerOnScrollListener: RecyclerOnScrollListener
    private val linearLayoutManager = GridLayoutManager(context, 4)

    private var scrolledY = 0

    var onItemClick: ((Int) -> Unit)? = null
    var onLimitReached: (() -> Unit)? = null

    init {
        recyclerOnScrollListener = object: RecyclerOnScrollListener(linearLayoutManager) {
            override fun limitReached() { onLimitReached?.invoke() }
        }

        charactersListAdapter = CharactersListAdapter { id -> onItemClick?.invoke(id) }

        adapter = charactersListAdapter
        layoutManager = linearLayoutManager

        addOnScrollListener(recyclerOnScrollListener)

        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrolledY += dy
            }
        })
    }

    override fun onSaveInstanceState(): Parcelable =
        SavedState(super.onSaveInstanceState()).apply {
            scrollY = this@CharactersListRecyclerView.scrolledY
        }

    override fun onRestoreInstanceState(state: Parcelable?) {
        scrolledY =
            if (state !is SavedState) {
                super.onRestoreInstanceState(state)
                0
            } else {
                super.onRestoreInstanceState(state.superState)
                state.scrollY
            }
    }

    fun addCharacters(characters: List<CharacterItem>) {
        charactersListAdapter.removeItemIf(charactersListAdapter.itemCount-1)
            { it is MessageItem }
        linearLayoutManager.spanCount = 4
        charactersListAdapter.addCharacters(characters)
    }

    fun showMessage(message: String) {
        if (charactersListAdapter.itemCount == 0)
            linearLayoutManager.spanCount = 1
        charactersListAdapter.showMessage(/*context.getString(R.string.loading_next_page_message)*/
            message
        )
    }

    fun onLoadFinished() = recyclerOnScrollListener.loadOk()

    fun clearList() = charactersListAdapter.removeAll()

    private class SavedState : BaseSavedState {

        var scrollY by Delegates.notNull<Int>()

        private constructor(parcel: Parcel) : super(parcel) {
            scrollY = parcel.readInt()
        }

        constructor(parcelable: Parcelable?) : super(parcelable)

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(scrollY)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }
}
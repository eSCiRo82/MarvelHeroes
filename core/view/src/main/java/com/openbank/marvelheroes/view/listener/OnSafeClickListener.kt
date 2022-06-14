package com.openbank.marvelheroes.view.listener

import android.os.SystemClock
import android.view.View

private const val defaultInterval = 1000L

/**
 * This is an implementation of the interface View.OnClickListener used to catch when the user
 * clicks on a view. This implementation introduces a delay after the click in order to deal with
 * repetitive clicks on the same view.
 */
class OnSafeClickListener(private val interval: Long = defaultInterval, private val onSafeClick: View.OnClickListener) :
    View.OnClickListener {
    private var lastClick: Long = 0

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - lastClick < interval) return
        lastClick = SystemClock.elapsedRealtime()
        onSafeClick.onClick(v)
    }
}
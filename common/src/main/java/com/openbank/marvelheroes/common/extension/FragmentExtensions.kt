package com.openbank.marvelheroes.common.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

inline fun <reified T : Fragment> FragmentManager.internalNavigation(
    @IdRes placeholder: Int,
    arguments: Bundle? = null,
    tag: String? = T::class.java.canonicalName,
    addToBackStack: Boolean = true,
) {
    this.beginTransaction()
        .replace(placeholder, T::class.java, arguments, tag ?: T::class.java.canonicalName)
        .apply { if (addToBackStack) addToBackStack(tag) }.commit()
}


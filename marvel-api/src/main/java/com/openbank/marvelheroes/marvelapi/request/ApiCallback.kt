
package com.openbank.marvelheroes.marvelapi.request

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCallback<T> constructor(
    private val onSuccess: ((T?) -> Unit)?,
    private val onFailure: ((t: Throwable?) -> Unit)?
): Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful)
            onSuccess?.invoke(response.body())
        else
            onFailure?.invoke(Throwable(response.message(), null))
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }
}
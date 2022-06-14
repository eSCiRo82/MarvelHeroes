package com.openbank.marvelheroes.marvelapi.request

import com.openbank.marvelheroes.marvelapi.response.ApiResponse
import com.openbank.marvelheroes.marvelapi.type.ApiResponseError
import com.openbank.marvelheroes.common.either.Either
import retrofit2.Call

abstract class ApiRequest<T> {
    private var call: Call<ApiResponse<T>>? = null

    protected abstract fun launchRequest(params: Map<String, Any>) : Call<ApiResponse<T>>

    fun requestSync(params: Map<String, Any>) : Either<ApiResponse<T>, ApiResponseError> {
        if (call == null || (call?.isExecuted == true || call?.isCanceled == true))
            call = launchRequest(params)

        call?.execute()?.let { response ->
            return if (response.isSuccessful)
                response.body()?.let { body ->
                    Either.Success(body)
                }
                    ?: Either.Failure(ApiResponseError.ApiResponseFailure("${response.code()}: ${response.message()}"))
            else
                Either.Failure(ApiResponseError.ApiResponseFailure("${response.code()}: ${response.message()}"))
        }
        return Either.Failure(ApiResponseError.ApiResponseCallNotCreated)
    }

    fun requestAsync(callback: ApiCallback<ApiResponse<T>>, params: Map<String, Any>) {
        if (call == null || (call?.isExecuted == true || call?.isCanceled == true))
            call = launchRequest(params)

        call?.enqueue(callback)
    }
}
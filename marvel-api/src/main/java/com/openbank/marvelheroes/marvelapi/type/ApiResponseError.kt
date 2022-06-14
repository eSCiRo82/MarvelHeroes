package com.openbank.marvelheroes.marvelapi.type

sealed class ApiResponseError {
    data class ApiResponseFailure(val message: String): ApiResponseError()
    object ApiResponseEmpty : ApiResponseError()
    object ApiResponseCallNotCreated : ApiResponseError()
    object ApiResponseCallNotFinished : ApiResponseError()
}

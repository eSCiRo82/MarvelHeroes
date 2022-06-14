package com.openbank.marvelheroes.data.datasource

import com.openbank.marvelheroes.marvelapi.request.CharactersRequest
import com.openbank.marvelheroes.marvelapi.response.CharacterObject
import com.openbank.marvelheroes.common.extension.mapSuccess
import com.openbank.marvelheroes.common.option.None
import com.openbank.marvelheroes.common.option.Option
import com.openbank.marvelheroes.common.option.Value
import java.util.*
import javax.inject.Inject

/**
 * Implementation of the data source required methods using a the remote API to obtain the data.
 */
class MarvelCharactersRemoteDataSource @Inject constructor(
    private val charactersRequest: CharactersRequest
) : CharactersDataSource {

    override fun get(id: Int): Option<CharacterObject> =
        charactersRequest.requestSync(mapOf("id" to id)).mapSuccess { response ->
            if (response.data.count == 1 && response.data.results[0].id == id)
                Value(response.data.results[0])
            else None
        } ?: None

    override fun get(startsWith: String?, limit: Int, offset: Int): CharactersDataSource.ListOutput {
        val params = mutableMapOf<String, Any>(
            "limit" to limit,
            "offset" to offset,
            "ts" to Calendar.getInstance().timeInMillis)
        startsWith?.let { params["startsWith"] = it }
        return charactersRequest.requestSync(params).mapSuccess {
            if (it.data.count > 0) CharactersDataSource.ListOutput(
                it.data.results, it.data.total, it.data.offset, it.data.count)
            else CharactersDataSource.ListOutput(listOf(), 0, 0, 0)
        } ?: CharactersDataSource.ListOutput(listOf(), 0, 0, 0)
    }

}
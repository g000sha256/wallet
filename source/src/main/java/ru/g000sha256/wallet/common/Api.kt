package ru.g000sha256.wallet.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.g000sha256.wallet.model.dto.CardDto

class Api @Inject constructor(private val gson: Gson, private val okHttpClient: OkHttpClient) {

    fun getCards(coroutineScope: CoroutineScope): List<CardDto> {
        coroutineScope.ensureActive()
        val request = Request.Builder()
            .get()
            .url("https://zibuhoker.ru/ifm/wallet/api.json")
            .build()
        val typeToken = object : TypeToken<List<CardDto>>() {}
        coroutineScope.ensureActive()
        return load(coroutineScope, request, typeToken)
    }

    private fun <T> load(coroutineScope: CoroutineScope, request: Request, typeToken: TypeToken<T>): T {
        coroutineScope.ensureActive()
        val call = okHttpClient.newCall(request)
        coroutineScope.ensureActive()
        val response = call.execute()
        coroutineScope.ensureActive()
        val responseBody = response.body!!
        if (response.isSuccessful) {
            coroutineScope.ensureActive()
            val inputStream = responseBody.byteStream()
            coroutineScope.ensureActive()
            val inputStreamReader = InputStreamReader(inputStream)
            coroutineScope.ensureActive()
            val result = gson.fromJson<T>(inputStreamReader, typeToken.type)
            coroutineScope.ensureActive()
            return result
        } else {
            coroutineScope.ensureActive()
            throw IOException(response.message)
        }
    }

}
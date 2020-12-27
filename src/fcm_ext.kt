package com.utsman

import com.utsman.data.FcmBody
import com.utsman.data.FcmResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun fcmSend(client: HttpClient, fcmData: Any, token: String? = null, topic: String = "default") =
    withContext(Dispatchers.Default) {
        client.post<FcmResponse> {
            url(fcmUrl)
            header("Authorization", Header.key)
            header("Content-Type", Header.contentType)
            contentType(ContentType.Application.Json)

            body = FcmBody(
                to = token ?: topic,
                data = fcmData
            )
        }
    }
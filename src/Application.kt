package com.utsman

import com.utsman.data.FcmData
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.client.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.*
import io.ktor.client.features.logging.*
import io.ktor.request.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    val client = HttpClient {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        install(Logging) {
            level = LogLevel.HEADERS
        }
    }

    runBlocking {
        // Sample for making a HTTP Client request
        /*
        val message = client.post<JsonSampleClass> {
            url("http://127.0.0.1:8080/path/to/endpoint")
            contentType(ContentType.Application.Json)
            body = JsonSampleClass(hello = "world")
        }
        */
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }

        post("/send") {
            val body = call.receive<SampleBodyRequest>().apply {
                additional = "from backend"
            }
            val token = body.token
            val fcmData = FcmData(body.title, body.message)
            val responsePost = fcmSend(client, fcmData, token)
            call.respond(responsePost)
        }
    }
}

val fcmUrl = "https://fcm.googleapis.com/fcm/send"
val authKey = "your auth key"

object Header {
    val key = "key=$authKey"
    val contentType = "application/json"
}

data class SampleBodyRequest(
    var token: String = "",
    var title: String = "",
    var message: String = "",
    var additional: String = ""
)

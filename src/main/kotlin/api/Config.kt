package api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

const val HOST = "localhost"
const val PORT = 5022

val client = HttpClient(CIO){
    install(ContentNegotiation)
    {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}

suspend fun isServerConnected(url: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            // Отправляем GET-запрос
            val response: HttpResponse = client.get(url)

            // Проверяем успешный код ответа (2xx)
            response.status.value in 200..299
        } catch (e: Exception) {
            // Обработка ошибок (например, сетевые ошибки)
            false
        }
    }
}

package api.payment

import api.GeneralResponse.FailResponse
import api.HOST
import api.PORT
import api.client
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun getAllPayment(
    rentalId: String? = null,
    email: String? = null,
    amount: String? = null,
    step: String? = null,
    paymentDate: String? = null,
    paymentMethod: String? = null,

    createAt: String? = null,
    sortBy: String = "Payments.paymentId",
    sortDirection: String = "ASC"  // "asc" or "desc"
): List<PaymentResponseViewDto> {
    val response: HttpResponse = client.request {
        url {
            method = HttpMethod.Get
            host = HOST // Замените на ваш хост
            port = PORT // Убедитесь, что порту соответствует вашему серверу
            path("/api/Payments/GetPaymentsForTable") // Укажите только путь к API

            // Добавьте параметры запроса
            if (rentalId != null) parameters["rentalId"] = rentalId
            if(email != null) parameters["email"] = email
            if(amount != null) parameters["amount"] = amount
            if(step != null) parameters["step"] = step
            if(paymentDate != null) parameters["paymentDate"] = paymentDate
            if(paymentMethod != null) parameters["paymentMethod"] = paymentMethod

            if (createAt != null) parameters["createAt"] = createAt
            parameters["sortBy"] = sortBy
            parameters["sortDirection"] = sortDirection
        }
        contentType(ContentType.Application.Json)
    }
    if (response.status.value == 200)
    {
        val result: List<PaymentResponseViewDto> = response.body()
        return result
    }
    else
    {
        val errorMessage = FailResponse(
            detail = response.body()
        )
        throw Exception(errorMessage.detail)
    }
}

suspend fun getPaymentById(
    paymentId: Long
): PaymentResponse {
    val response: HttpResponse = client.request {
        url {
            method = HttpMethod.Get
            host = HOST // Замените на ваш хост
            port = PORT // Убедитесь, что порту соответствует вашему серверу
            path("/api/Payments/GetPayment/${paymentId}") // Укажите только путь к API

        }
        contentType(ContentType.Application.Json)
    }
    if (response.status.value == 200)
    {
        val result: PaymentResponse = response.body()
        return result
    }
    else
    {
        val errorMessage = FailResponse(
            detail = response.body()
        )
        throw Exception(errorMessage.detail)
    }
}

suspend fun postPayment(payment: PaymentRequest): PaymentResponse {
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Post
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            path("api/Payments/InsertPayment")

        }
        contentType(ContentType.Application.Json)
        setBody(payment)
    }

    if (response.status.value in 200..299) {
        val result: PaymentResponse = response.body()
        return result
    }
    else {
        val result: FailResponse = response.body()
        throw Exception(result.detail)
    }
}

suspend fun putPayment(id: Long, payment: PaymentRequest): PaymentResponse{
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Put
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            path("api/Payments/UpdatePayment/${id}")

        }
        contentType(ContentType.Application.Json)
        setBody(payment)
    }

    if (response.status.value in 200..299) {
        val result: PaymentResponse = response.body()
        return result
    }
    else {
        val result: FailResponse = response.body()
        throw Exception(result.detail)
    }
}

suspend fun deletePayment(id: Long) {
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Delete
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            path("api/Payments/DeletePayment/${id}")

        }
        contentType(ContentType.Application.Json)
    }
    (response.status.value)
    if (response.status.value in 200..299) {
        return
    }
    else {
        val result: FailResponse = response.body()
        throw Exception(result.detail)
    }
}
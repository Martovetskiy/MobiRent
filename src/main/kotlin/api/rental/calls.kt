package api.rental

import api.GeneralResponse.FailResponse
import api.HOST
import api.PORT
import api.client
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun getAllRental(
    rentalId: String? = null,
    email: String? = null,
    make: String? = null,
    model: String? = null,
    startDate: String? = null,
    endDate: String? = null,
    totalPrice: String? = null,
    createAt: String? = null,
    sortBy: String = "Rentals.rentalId",
    sortDirection: String = "ASC"  // "asc" or "desc"
): List<RentalResponse> {
    val response: HttpResponse = client.request {
        url {
            method = HttpMethod.Get
            host = HOST // Замените на ваш хост
            port = PORT // Убедитесь, что порту соответствует вашему серверу
            path("/api/Rentals/GetRentals") // Укажите только путь к API

            // Добавьте параметры запроса
            if (rentalId != null) parameters["rentalId"] = rentalId
            if(email != null) parameters["email"] = email
            if(make != null) parameters["make"] = make
            if(model != null) parameters["model"] = model
            if(startDate != null) parameters["startDate"] = startDate
            if(endDate != null) parameters["endDate"] = endDate
            if(totalPrice != null) parameters["totalPrice"] = totalPrice

            if (createAt != null) parameters["createAt"] = createAt
            parameters["sortBy"] = sortBy
            parameters["sortDirection"] = sortDirection
        }
        contentType(ContentType.Application.Json)
    }
    if (response.status.value == 200)
    {
        val result: List<RentalResponse> = response.body()
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

suspend fun postRental(rental: RentalRequest): RentalResponse {
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Post
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            path("api/Rentals/InsertRental")

        }
        contentType(ContentType.Application.Json)
        setBody(rental)
    }

    if (response.status.value in 200..299) {
        val result: RentalResponse = response.body()
        return result
    }
    else {
        val result: FailResponse = response.body()
        throw Exception(result.detail)
    }
}

suspend fun putRental(rental: RentalResponse): RentalResponse{
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Put
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            path("api/Rentals/UpdateRental/${rental.rentalId}")

        }
        contentType(ContentType.Application.Json)
        setBody(rental)
    }

    if (response.status.value in 200..299) {
        val result: RentalResponse = response.body()
        return result
    }
    else {
        val result: FailResponse = response.body()
        throw Exception(result.detail)
    }
}

suspend fun deleteRental(id: Long): RentalResponse? {
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Delete
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            path("api/Rentals/DeleteRental/${id}")

        }
        contentType(ContentType.Application.Json)
    }
    (response.status.value)
    if (response.status.value in 200..299) {
        if (response.status.value == 200){
            val result = null
            return result
        }
        else{
            val result: RentalResponse = response.body()
            return result
        }
    }
    else {
        val result: FailResponse = response.body()

        throw Exception(result.detail)
    }
}
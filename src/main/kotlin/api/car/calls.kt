package api.car

import api.GeneralResponse.FailResponse
import api.HOST
import api.PORT
import api.client
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun getAllCar(
    carId: String? = null,
    make: String? = null,
    model: String? = null,
    year: String? = null,
    colorHex: String? = null,
    pricePerDay: String? = null,
    numberPlate: String? = null,
    status: String? = null,
    createAt: String? = null,
    sortBy: String = "make",
    sortDirection: String = "ASC"  // "asc" or "desc"
): List<CarResponse> {
    val response: HttpResponse = client.request {
        url {
            method = HttpMethod.Get
            host = HOST // Замените на ваш хост
            port = PORT // Убедитесь, что порту соответствует вашему серверу
            path("/api/Cars/GetCars") // Укажите только путь к API

            // Добавьте параметры запроса
            if (carId != null) parameters["carId"] = carId
            if (make != null) parameters["make"] = make
            if (model != null) parameters["model"] = model
            if (year!= null) parameters["year"] = year
            if (colorHex!= null) parameters["colorHex"] = colorHex
            if (pricePerDay!= null) parameters["pricePerDay"] = pricePerDay
            if (numberPlate!= null) parameters["numberPlate"] = numberPlate
            if (status!= null) parameters["status"] = status
            if (createAt != null) parameters["createAt"] = createAt
            parameters["sortBy"] = sortBy
            parameters["sortDirection"] = sortDirection
        }
        contentType(ContentType.Application.Json)
    }
    if (response.status.value == 200)
    {
        val result: List<CarResponse> = response.body()
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

suspend fun postCar(car: CarRequest): CarResponse {
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Post
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            path("api/Cars/InsertCar")

        }
        contentType(ContentType.Application.Json)
        setBody(car)
    }

    if (response.status.value in 200..299) {
        val result: CarResponse = response.body()
        return result
    }
    else {
        val result: FailResponse = response.body()
        throw Exception(result.detail)
    }
}

suspend fun putCar(car: CarResponse): CarResponse{
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Put
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            path("api/Cars/UpdateCar/${car.carId}")

        }
        contentType(ContentType.Application.Json)
        setBody(car)
    }

    if (response.status.value in 200..299) {
        val result: CarResponse = response.body()
        return result
    }
    else {
        val result: FailResponse = response.body()
        throw Exception(result.detail)
    }
}

suspend fun deleteCar(id: Long): CarResponse? {
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Delete
            protocol = URLProtocol.HTTP
            host = HOST
            port = PORT
            path("api/Cars/DeleteCar/${id}")

        }
        contentType(ContentType.Application.Json)
    }

    if (response.status.value in 200..299) {
        if (response.status.value == 200){
            val result = null
            return result
        }
        else{
            val result: CarResponse = response.body()
            return result
        }
    }
    else {
        val result: FailResponse = response.body()

        throw Exception(result.detail)
    }
}
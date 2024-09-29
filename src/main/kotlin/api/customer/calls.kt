package api.customer

import api.GeneralResponse.FailResponse
import api.HOST
import api.client
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

suspend fun getAllCustomer(
    customerId: String? = null,
    firstName: String? = null,
    lastName: String? = null,
    email: String? = null,
    phoneNumber: String? = null,
    driverLicense: String? = null,
    isBanned: Boolean? = null,
    createAt: String? = null,
    sortBy: String = "firstName",
    sortDirection: String = "ASC"  // "asc" or "desc"
): List<CustomerResponse> {
    val response: HttpResponse = client.request {
        url {
            method = HttpMethod.Get
            host = HOST // Замените на ваш хост
            port = 5022 // Убедитесь, что порту соответствует вашему серверу
            path("/api/Customers/GetCustomers") // Укажите только путь к API

            // Добавьте параметры запроса
            if (customerId != null) parameters["customerId"] = customerId
            if (firstName != null) parameters["firstName"] = firstName
            if (lastName != null) parameters["lastName"] = lastName
            if (email != null) parameters["email"] = email
            if (phoneNumber != null) parameters["phoneNumber"] = phoneNumber
            if (driverLicense != null) parameters["driverLicense"] = driverLicense
            if (isBanned != null) parameters["isBanned"] = isBanned.toString()
            if (createAt != null) parameters["createAt"] = createAt
            parameters["sortBy"] = sortBy
            parameters["sortDirection"] = sortDirection
        }
        contentType(ContentType.Application.Json)
    }
    if (response.status.value == 200)
    {
        val result: List<CustomerResponse> = response.body()
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

suspend fun postCustomer(customerRequest: CustomerRequest): CustomerResponse {
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Post
            protocol = URLProtocol.HTTP
            host = HOST
            port = 5022
            path("api/Customers/InsertCustomer")

        }
        contentType(ContentType.Application.Json)
        setBody(customerRequest)
    }

    if (response.status.value in 200..299) {
        val result: CustomerResponse = response.body()
        return result
    }
    else {
        val result: FailResponse = response.body()
        throw Exception(result.detail)
    }
}

suspend fun putCustomer(customer: CustomerResponse): CustomerResponse{
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Put
            protocol = URLProtocol.HTTP
            host = HOST
            port = 5022
            path("api/Customers/UpdateCustomer/${customer.customerId}")

        }
        contentType(ContentType.Application.Json)
        setBody(customer)
    }

    if (response.status.value in 200..299) {
        val result: CustomerResponse = response.body()
        return result
    }
    else {
        val result: FailResponse = response.body()
        throw Exception(result.detail)
    }
}

suspend fun deleteCustomer(id: Long): CustomerResponse? {
    val response: HttpResponse = client.request{
        url {
            method = HttpMethod.Delete
            protocol = URLProtocol.HTTP
            host = HOST
            port = 5022
            path("api/Customers/DeleteCustomer/${id}")

        }
        contentType(ContentType.Application.Json)
    }
    println(response.status.value)
    if (response.status.value in 200..299) {
        if (response.status.value == 200){
            val result = null
            return result
        }
        else{
            val result: CustomerResponse = response.body()
            return result
        }
    }
    else {
        val result: FailResponse = response.body()

        throw Exception(result.detail)
    }
}
package api.rental

import api.car.CarResponse
import api.customer.CustomerResponse
import api.serial.OffsetDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class RentalResponse (
    @SerialName("rentalId")
    val rentalId: Long,
    @SerialName("customerId")
    val customerId: Long,
    @SerialName("customer")
    val customer: CustomerResponse? = null,
    @SerialName("carId")
    val carId: Long,
    @SerialName("car")
    val car: CarResponse? = null,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val startDate: OffsetDateTime?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val endDate: OffsetDateTime?,
    @SerialName("totalPrice")
    val totalPrice: Double?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createAt: OffsetDateTime?
)
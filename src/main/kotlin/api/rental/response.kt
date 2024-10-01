package api.rental

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
    @SerialName("carId")
    val carId: Long,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val startDate: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val endDate: OffsetDateTime,
    @SerialName("totalPrice")
    val totalPrice: Double?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createAt: OffsetDateTime
)

@Serializable
data class RentalResponseViewDto (
    @SerialName("rentalId")
    val rentalId: Long,
    @SerialName("customerId")
    val customerId: Long,
    @SerialName("carId")
    val carId: Long,
    @SerialName("email")
    val email: String,
    @SerialName("make")
    val make: String,
    @SerialName("model")
    val model: String,

    @Serializable(with = OffsetDateTimeSerializer::class)
    val startDate: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val endDate: OffsetDateTime,
    @SerialName("totalPrice")
    val totalPrice: Double?,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createAt: OffsetDateTime
)
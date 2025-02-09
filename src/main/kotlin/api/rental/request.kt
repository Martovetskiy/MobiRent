package api.rental

import api.serial.OffsetDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class RentalRequest (
    @SerialName("customerId")
    val customerId: Long,
    @SerialName("carId")
    val carId: Long,
    @SerialName("startDate")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val startDate: OffsetDateTime,
    @SerialName("endDate")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val endDate: OffsetDateTime,
    @SerialName("createAt")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createAt: OffsetDateTime
)
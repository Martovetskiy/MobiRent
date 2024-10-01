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
    @Serializable(with = OffsetDateTimeSerializer::class)
    val startDate: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val endDate: OffsetDateTime,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createAt: OffsetDateTime
)
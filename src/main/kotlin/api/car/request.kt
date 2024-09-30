package api.car

import api.serial.OffsetDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class CarRequest (
    @SerialName("make")
    val make: String,
    @SerialName("model")
    val model: String,
    @SerialName("year")
    val year: Int,
    @SerialName("colorHex")
    val colorHex: String,
    @SerialName("pricePerDay")
    val pricePerDay: Double,
    @SerialName("numberPlate")
    val numberPlate: String,
    @SerialName("status")
    val status: String,
    @SerialName("createAt")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createAt: OffsetDateTime
)
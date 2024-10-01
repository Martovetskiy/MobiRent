package api.payment

import api.serial.OffsetDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class PaymentResponse (
    @SerialName("paymentId")
    val paymentId: Long,
    @SerialName("rentalId")
    val rentalId: Long,
    @SerialName("amount")
    val amount: Double,
    @SerialName("step")
    val step: Int,
    @SerialName("paymentDate")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val paymentDate: OffsetDateTime,
    @SerialName("paymentMethod")
    val paymentMethod: String,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createAt: OffsetDateTime
)

@Serializable
data class PaymentResponseViewDto (
    @SerialName("paymentId")
    val paymentId: Long,
    @SerialName("rentalId")
    val rentalId: Long,
    @SerialName("email")
    val email: String,
    @SerialName("amount")
    val amount: Double,
    @SerialName("step")
    val step: Int,
    @SerialName("paymentDate")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val paymentDate: OffsetDateTime,
    @SerialName("paymentMethod")
    val paymentMethod: String,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createAt: OffsetDateTime
)
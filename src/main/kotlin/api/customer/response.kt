package api.customer

import api.serial.OffsetDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class CustomerResponse (
    @SerialName("customerId")
    val customerId: Long,
    @SerialName("firstName")
    var firstName: String,
    @SerialName("lastName")
    var lastName: String,
    @SerialName("email")
    var email: String,
    @SerialName("phoneNumber")
    var phoneNumber: String,
    @SerialName("driverLicense")
    var driverLicense: String,
    @SerialName("isBanned")
    val isBanned: Boolean = false,
    @SerialName("createAt")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val createAt: OffsetDateTime
)
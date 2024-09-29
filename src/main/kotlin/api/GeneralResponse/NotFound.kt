package api.GeneralResponse

import kotlinx.serialization.Serializable

@Serializable
data class FailResponse(
    val detail: String
)
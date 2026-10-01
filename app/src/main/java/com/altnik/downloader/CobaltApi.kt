package com.altnik.downloader

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class CobaltRequest(
    val url: String,
    val videoQuality: String = "720",
    val filenameStyle: String = "basic"
)

data class PickerItem(
    val url: String? = null,
    val thumb: String? = null,
    val type: String? = null
)

data class CobaltError(val code: String? = null)

data class CobaltResponse(
    val status: String? = null,      // "redirect" | "tunnel" | "picker" | "error"
    val url: String? = null,         // direct file URL when redirect/tunnel
    val filename: String? = null,
    val picker: List<PickerItem>? = null,
    val error: CobaltError? = null
)

interface CobaltApi {
    @Headers("Accept: application/json", "Content-Type: application/json")
    // Cobalt v10+: process requests at POST / (not /api/json).
    @POST("/")
    suspend fun resolve(@Body body: CobaltRequest): CobaltResponse
}

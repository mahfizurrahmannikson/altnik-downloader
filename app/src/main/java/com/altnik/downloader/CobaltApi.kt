package com.altnik.downloader

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class CobaltRequest(
    val url: String,
    val videoQuality: String = "720",
    val filenameMode: String = "basic"
)

data class PickerItem(
    val url: String? = null,
    val thumb: String? = null,
    val type: String? = null
)

data class CobaltResponse(
    val status: String? = null,      // "redirect" | "tunnel" | "picker" | "error"
    val url: String? = null,         // direct file url when redirect/tunnel
    val picker: List<PickerItem>? = null,
    val pickerType: String? = null,
    val text: String? = null         // error message
)

interface CobaltApi {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/json")
    suspend fun resolve(@Body body: CobaltRequest): CobaltResponse
}

package com.simats.quitsmoking.network

import retrofit2.http.Body
import retrofit2.http.POST

data class AiRequest(
    val question: String
)

data class AiResponse(
    val success: Boolean,
    val reply: String
)

interface AiApi {

    @POST("quit_smoking_ai.php")
    suspend fun askAi(
        @Body request: AiRequest
    ): AiResponse
}

package com.example.mvp2.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterResponse(
        @Json(name="status")
        var status: String,
        @Json(name="message")
        var message: String
)
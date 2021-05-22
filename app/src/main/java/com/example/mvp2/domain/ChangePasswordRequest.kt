package com.example.mvp2.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChangePasswordRequest(
        @Json(name="old_password")
        var oldPass: String,
        @Json(name="new_password")
        var newPass: String
)
package com.example.mvp2.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerifyResponse(
    @Json(name = "status")
    var status: String,
    @Json(name = "data")
    var userInfo: UserInfo
    )
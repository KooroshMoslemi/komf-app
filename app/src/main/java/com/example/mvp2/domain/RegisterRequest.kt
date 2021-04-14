package com.example.mvp2.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequest(
        @Json(name="fname")
        var firstName: String,
        @Json(name="lname")
        var lastName: String,
        @Json(name="email")
        var email: String,
        @Json(name="phone")
        var phone: String,
        @Json(name= "password")
        var password: String
)
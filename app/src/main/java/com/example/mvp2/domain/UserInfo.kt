package com.example.mvp2.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class UserInfo(
        @Json(name = "fname")
        var userFirstName: String,
        @Json(name = "lname")
        var userLastName: String,
        @Json(name = "email")
        var userEmail: String,
        @Json(name = "phone")
        var userPhone: String,
        @Json(name = "role")
        var userRole: String,
        @Json(name = "photo_url")
        var userPhotoUrl: String
): Parcelable

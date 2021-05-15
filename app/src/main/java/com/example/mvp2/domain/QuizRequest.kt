package com.example.mvp2.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuizRequest(
        @Json(name="limit")
        var limit: Int,
        @Json(name="lessons_id")
        var lessons_id: String
)
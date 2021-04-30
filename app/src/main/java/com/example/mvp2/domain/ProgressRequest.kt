package com.example.mvp2.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProgressRequest(
        @Json(name="lesson_id")
        var lessonId: String,
        @Json(name= "vocab_id")
        var vocabId: String
)
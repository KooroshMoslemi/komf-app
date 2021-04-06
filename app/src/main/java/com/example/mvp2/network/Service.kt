/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mvp2.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


enum class ApiStatus { LOADING, ERROR, DONE }

interface Service {
    @GET("ffacbda3")
    fun getVocabs(): Deferred<NetworkVocabContainer>
    @GET("f96d8aba")
    fun getLessons(): Deferred<NetworkLessonContainer>
    @GET("72b7cc84")
    fun getQuiz(): Deferred<NetworkQuizContainer>
    @GET("d083f1e6")
    fun getPopularCourses(): Deferred<NetworkPopularCoursesContainer>
}

object Network {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.mocki.io/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val instance = retrofit.create(Service::class.java)

}



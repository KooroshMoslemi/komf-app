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


import com.example.mvp2.domain.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


enum class ApiStatus { LOADING, ERROR, DONE }

interface Service {
    @GET("ffacbda3")
    fun getVocabs(): Deferred<NetworkVocabContainer>

    @GET("f96d8aba")
    fun getLessons(): Deferred<NetworkLessonContainer>

    @GET("72b7cc84")
    fun getQuiz(): Deferred<NetworkQuizContainer>

    @GET("api/courses")
    fun verifyToken(
            @Header("Authorization") authToken: String
    ): Deferred<VerifyTokenResponse>

    @GET("api/courses")
    fun getPopularCourses(
            @Header("Authorization") authToken: String
    ): Deferred<NetworkPopularCoursesContainer>

    @POST("api/login")
    fun login(@Body request: LoginRequest): Deferred<LoginResponse>

    @POST("api/register")
    fun register(@Body request: RegisterRequest): Deferred<RegisterResponse>

//
//    @Headers("Accept: application/json")
//    @POST("api/login")
//    fun login(
//            @Header("Authorization") authToken: String,
//            @Body request: LoginRequest
//    ): Deferred<LoginResponse>
}

object Network {


    var httpClient = OkHttpClient.Builder().addInterceptor { chain ->
        chain.proceed(
                chain.request().newBuilder().addHeader("Accept","application/json").build()
        )
    }

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
            //.baseUrl("https://api.mocki.io/v1/")
            .baseUrl("https://api.komf.ir/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(httpClient.build())
            .build()

    val instance = retrofit.create(Service::class.java)

}



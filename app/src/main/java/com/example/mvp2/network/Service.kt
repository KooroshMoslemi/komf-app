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
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


enum class ApiStatus { LOADING, ERROR, DONE }

interface Service {
    @GET("api/lessons/{lessons_id}/vocabs")
    fun getVocabs(
            @Header("Authorization") authToken: String,
            @Path("lessons_id") lessonId: Long
    ): Deferred<NetworkVocabContainer>

    @GET("api/courses/{course_id}/lessons")
    fun getLessons(
            @Header("Authorization") authToken: String,
            @Path("course_id") courseId: Long
    ): Deferred<NetworkLessonContainer>

    @POST("api/lessons/quiz")
    fun getQuiz(
            @Header("Authorization") authToken: String,
            @Body request: QuizRequest
    ): Deferred<NetworkQuizContainer>

    @GET("api/user")
    fun verifyToken(
            @Header("Authorization") authToken: String
    ): Deferred<VerifyResponse>

    @POST("api/user/UpdateUser")
    fun updateUser(
            @Header("Authorization") authToken: String,
            @Body request: UpdateUserRequest
    ): Deferred<VerifyResponse>

    @GET("api/user/courses")
    fun getMyCourses(
            @Header("Authorization") authToken: String
    ): Deferred<NetworkMyCoursesContainer>

    @GET("api/courses")
    fun getAllCourses(
            @Header("Authorization") authToken: String
    ): Deferred<NetworkCoursesContainer>

    @POST("api/login")
    fun login(@Body request: LoginRequest): Deferred<LoginResponse>

    @POST("api/logout")
    fun logout(
            @Header("Authorization") authToken: String
    ): Deferred<GeneralResponse>

    @POST("api/register")
    fun register(@Body request: RegisterRequest): Deferred<RegisterResponse>

    @POST("api/courses/add/{course_id}")
    fun enroll(
            @Header("Authorization") authToken: String,
            @Path("course_id") courseId: Long
    ): Deferred<GeneralResponse>

    @POST("api/courses/remove/{course_id}")
    fun unroll(
            @Header("Authorization") authToken: String,
            @Path("course_id") courseId: Long
    ): Deferred<GeneralResponse>

    @POST("api/lesson/vocab/check")
    fun submitVocabProgress(
        @Header("Authorization") authToken: String,
        @Body request: ProgressRequest
    ): Deferred<GeneralResponse>


    @POST("api/user/ChangePassword")
    fun changePassword(
        @Header("Authorization") authToken: String,
        @Body request: ChangePasswordRequest
    ): Deferred<GeneralResponse>


//    @Multipart
//    @POST("api/user/ChangeProfile")
//    fun uploadProfileImage(
//            @Header("Authorization") authToken: String,
//            @Part("file") file: MultipartBody.Part
//    ): Deferred<GeneralResponse>

    @POST("api/user/ChangeProfile")
    fun uploadProfileImage(
            @Header("Authorization") authToken: String,
            @Body file: RequestBody
    ): Deferred<GeneralResponse>

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



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

import com.example.mvp2.course.CourseStatus
import com.example.mvp2.database.DatabaseVocab
import com.example.mvp2.domain.*
import com.example.mvp2.lesson.LessonStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkPopularCoursesContainer(val data: List<NetworkPopularCourse>)

@JsonClass(generateAdapter = true)
data class NetworkQuizContainer(val quiz: NetworkQuiz)

@JsonClass(generateAdapter = true)
data class NetworkVocabContainer(val vocabs: List<NetworkVocab>)


@JsonClass(generateAdapter = true)
data class NetworkLessonContainer(val lessons: List<NetworkLesson>)


@JsonClass(generateAdapter = true)
data class NetworkQuiz(
        val time:Int,
        val questions:List<NetworkQuestion> = ArrayList()
)


@JsonClass(generateAdapter = true)
data class NetworkQuestion(
        @Json(name="text")
        val questionText:String,
        @Json(name="ch1")
        val choiceOne:String,
        @Json(name="ch2")
        val choiceTwo:String,
        @Json(name="ch3")
        val choiceThree:String,
        @Json(name="ch4")
        val choiceFour:String,
        @Json(name="correct_choice")
        val correctChoice:Int
)


@JsonClass(generateAdapter = true)
data class NetworkVocab(
    @Json(name="vocab_id")
    val vocabId:Long,
    @Json(name="lesson_id")
    val lessonId:Long,
    val word:String,
    val syn:String,
    val def:String,
    val ex1:String,
    val ex2:String
)


@JsonClass(generateAdapter = true)
data class NetworkPopularCourse(
        @Json(name="id")
        val courseId : Long,
        @Json(name="title")
        val courseTitle : String,
        @Json(name="description")
        val courseDescription : String,
        @Json(name="price")
        val coursePrice : Float
//        @Json(name="photo_url")
//        val photoUrl : String,
//        @Json(name="status")
//        val availabilityStatus : String
)


@JsonClass(generateAdapter = true)
data class NetworkLesson(
    @Json(name="lesson_id")
    val lessonId:Long,
    @Json(name="lesson_title")
    val lessonTitle:String,
    val progress:Int,
    @Json(name="total_words")
    val totalWords:String,
    @Json(name="word_ids")
    val wordIds:List<Long>
)


fun NetworkQuizContainer.asDomainModel(): Quiz {

    val questionsList : List<Question> = quiz.questions.map {
        Question(
                questionText = it.questionText,
                choiceOne = it.choiceOne,
                choiceTwo = it.choiceTwo,
                choiceThree = it.choiceThree,
                choiceFour = it.choiceFour,
                correctChoice = it.correctChoice)
    }

    val questions = ArrayList<Question>()
    questions.addAll(questionsList)

    return Quiz(
            time = quiz.time,
            questions =  questions
    )
}


fun NetworkVocabContainer.asDatabaseModel(): List<DatabaseVocab> {
    return vocabs.map {
        DatabaseVocab(
            vocabId = it.vocabId,
            lessonId = it.lessonId,
            word = it.word,
            syn = it.syn,
            def = it.def,
            ex1 = it.ex1,
            ex2 = it.ex2)
    }
}


fun NetworkLessonContainer.asDomainModel(): List<Lesson> {
    return lessons.map {

        var status:LessonStatus = LessonStatus.ACTIVE

        if(it.progress == 100){
            status = LessonStatus.COMPLETED
        }
        else if (it.progress == 0){
            status = LessonStatus.INACTIVE
        }

        Lesson(
            lessonId = it.lessonId,
            lessonTitle = it.lessonTitle,
            lessonProgress = it.progress,
            lessonStatus = status,
            remainedVocabIds = it.wordIds
        )
    }
}


fun NetworkPopularCoursesContainer.asDomainModel(): List<Course> {
    return data.map {

//        var status:CourseStatus = CourseStatus.Open
//
//        if(it.availabilityStatus == "restricted"){
//            status = CourseStatus.Restricted
//        }
//        else if (it.availabilityStatus == "not_ready"){
//            status = CourseStatus.NotReady
//        }

        Course(
                it.courseId,
                it.courseTitle,
                it.courseDescription,
                "https://i.imgur.com/JI9hOki.jpg",//Todo: update this when course photo is implemented in backened
                it.coursePrice
        )
    }
}



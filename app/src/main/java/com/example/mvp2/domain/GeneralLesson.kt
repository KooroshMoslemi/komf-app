package com.example.mvp2.domain

import android.os.Parcelable
import com.example.mvp2.lesson.LessonStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeneralLesson(
    val lessonId : Long,
    val lessonTitle : String): Parcelable


//val lessons : List<Lesson> = listOf(
//    Lesson(1,"Lesson 1" , 100, LessonStatus.COMPLETED),
//    Lesson(2,"Lesson 2" , 100, LessonStatus.COMPLETED),
//    Lesson(3,"Lesson 3" , 50, LessonStatus.ACTIVE),
//    Lesson(4,"Lesson 4" , 0, LessonStatus.INACTIVE),
//    Lesson(5,"Lesson 5" , 0, LessonStatus.INACTIVE)
//)


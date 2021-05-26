package com.example.mvp2.domain

import android.os.Parcelable
import com.example.mvp2.course.CourseStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Course(
        val courseId : Long,
        val courseTitle : String,
        val courseDescription: String,
        val photoUrl : String,
        val price : Float,
        val lessons : List<GeneralLesson>?
        //val availabilityStatus : CourseStatus
):Parcelable{
    override fun toString(): String = courseTitle
}

//val courses : List<Course> = listOf(
//        Course(1,"Barrons - Essential Words for TOEFL" , "https://i.imgur.com/JI9hOki.jpg", CourseStatus.Open),
//        Course(2,"400 must have words for the TOEFL test" , "https://imgur.com/N3CuR53.jpg", CourseStatus.Restricted),
//        Course(3,"The world of words" , "https://i.imgur.com/374A7sN.jpg", CourseStatus.NotReady),
//        Course(4,"504 absolutely essential words" , "https://imgur.com/l6CDzcv.jpg", CourseStatus.NotReady)
//)
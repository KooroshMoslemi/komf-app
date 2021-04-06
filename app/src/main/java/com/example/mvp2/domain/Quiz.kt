package com.example.mvp2.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//@Parcelize
//class Quiz: ArrayList<Question>(), Parcelable

@Parcelize
data class Quiz(
        val questions : ArrayList<Question>,
        val time : Int
):Parcelable
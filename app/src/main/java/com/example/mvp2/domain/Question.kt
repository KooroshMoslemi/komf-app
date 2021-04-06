package com.example.mvp2.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
        val questionText : String,
        val choiceOne: String,
        val choiceTwo: String,
        val choiceThree: String,
        val choiceFour: String,
        val correctChoice: Int
): Parcelable
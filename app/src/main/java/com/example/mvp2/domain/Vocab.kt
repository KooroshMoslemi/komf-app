package com.example.mvp2.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Vocab(val vocabId:Long,
                 val lessonId:Long,
                 val word:String,
                 val syn:String,
                 val def:String,
                 val ex1:String,
                 val ex2:String
                ): Parcelable
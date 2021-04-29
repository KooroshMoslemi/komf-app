package com.example.mvp2.domain

import android.os.Parcelable
import com.example.mvp2.lesson.LessonStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VocabList(
        val vocabs : ArrayList<Vocab>,
        val lessonStatus: LessonStatus
): Parcelable
package com.example.mvp2.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvp2.database.VocabDao
import com.example.mvp2.domain.Lesson
import com.example.mvp2.domain.Vocab
import com.example.mvp2.lesson.LessonStatus

class FlashcardViewModelFactory(private val vocabs: ArrayList<Vocab>, private val lessonStatus: LessonStatus,private val lessonId: Long, private val dataSource:VocabDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardViewModel::class.java)) {
            return FlashcardViewModel(vocabs,lessonStatus,lessonId,dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.mvp2.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvp2.database.VocabDao
import com.example.mvp2.domain.Lesson

class FlashcardViewModelFactory(private val lesson: Lesson, private val dataSource:VocabDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlashcardViewModel::class.java)) {
            return FlashcardViewModel(lesson,dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.mvp2.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvp2.database.VocabDao
import com.example.mvp2.domain.Lesson
import com.example.mvp2.domain.Quiz
import com.example.mvp2.flashcard.FlashcardViewModel

class QuizViewModelFactory(private val quiz: Quiz) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(quiz) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
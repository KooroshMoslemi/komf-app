package com.example.mvp2.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvp2.database.VocabDao
import com.example.mvp2.domain.Lesson
import com.example.mvp2.domain.Quiz
import com.example.mvp2.flashcard.FlashcardViewModel

class SearchViewModelFactory(private val authToken: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(authToken) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
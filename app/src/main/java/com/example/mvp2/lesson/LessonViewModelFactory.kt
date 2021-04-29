package com.example.mvp2.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LessonViewModelFactory(val authToken: String,val courseId : Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LessonViewModel(authToken,courseId) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}
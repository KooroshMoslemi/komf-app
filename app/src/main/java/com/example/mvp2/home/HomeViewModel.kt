package com.example.mvp2.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.mvp2.database.getDatabase
import com.example.mvp2.domain.Course
import com.example.mvp2.network.Network
import com.example.mvp2.network.asDomainModel
import com.example.mvp2.repository.VocabsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError


    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    private val _navigateToLesson = MutableLiveData<Boolean?>()

    val navigateToLesson: LiveData<Boolean?>
        get() = _navigateToLesson


    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>>
        get() = _courses


    private val vocabsRepository = VocabsRepository(getDatabase(application))
    val vocabs = vocabsRepository.vocabs

    init {
        getCourses()
        refreshDataFromRepository()
    }


    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                vocabsRepository.refreshVocabs()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                if(vocabs.value.isNullOrEmpty())
                    Log.e("error",networkError.message.toString())
                    _eventNetworkError.value = true
            }
        }
    }

    private fun getCourses(){
        Log.e("HomeViewModel","getting courses")
        viewModelScope.launch {
            try {
                _courses.value = Network.instance.getPopularCourses().await().asDomainModel()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                if(courses.value.isNullOrEmpty())
                    Log.e("error",networkError.message.toString())
                _eventNetworkError.value = true
            }
        }
    }


    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    fun doneLessonNavigating() {
        _navigateToLesson.value = null
    }

    fun onLessonNavigating(){
        _navigateToLesson.value = true
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}
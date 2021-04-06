package com.example.mvp2.setup

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvp2.domain.Lesson
import com.example.mvp2.domain.Question
import com.example.mvp2.domain.Quiz
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.network.Network
import com.example.mvp2.network.asDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class SetupViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val _quiz = MutableLiveData<Quiz>()
    val quiz : LiveData<Quiz> get() = _quiz

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status


    private val _navigateToQuiz = MutableLiveData<Boolean?>()

    val navigateToQuiz: LiveData<Boolean?>
        get() = _navigateToQuiz



    fun prepareQuiz(){
        coroutineScope.launch {
            var quizDeferred = Network.instance.getQuiz()
            try {
                val result = quizDeferred.await().asDomainModel()
                _quiz.value = result
                _status.value = ApiStatus.DONE
                //onLessonNavigating()
            }
            catch (e: Exception){
                Log.e("Error",e.message.toString())
                _quiz.value = null
                _status.value = ApiStatus.ERROR
            }
        }
    }


    fun doneLessonNavigating() {
        _navigateToQuiz.value = null
    }

    fun onLessonNavigating(){
        _navigateToQuiz.value = true
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
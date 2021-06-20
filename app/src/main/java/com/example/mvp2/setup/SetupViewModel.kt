package com.example.mvp2.setup

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvp2.domain.*
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

    private val _courses = MutableLiveData<List<Course>>()
    val courses : LiveData<List<Course>> get() = _courses


    private val _selectedCourse = MutableLiveData<Course>()
    val selectedCourse : LiveData<Course> get() = _selectedCourse


    private val _navigateToQuiz = MutableLiveData<Boolean?>()

    val navigateToQuiz: LiveData<Boolean?>
        get() = _navigateToQuiz


    fun selectCourse(course: Course){
        _selectedCourse.value = course
    }


    fun prepareQuiz(authToken:String, limit: Int,lessons_id:String){
        coroutineScope.launch {
            var quizDeferred = Network.instance.getQuiz("Bearer $authToken",QuizRequest(limit,lessons_id))
            try {
                val result = quizDeferred.await().asDomainModel()
                result.time = result.questions.size //Todo: you can fix it when api returns time
                _quiz.value = result
                _status.value = ApiStatus.DONE
                //onLessonNavigating()
            }
            catch (e: Exception){
                Log.e("Error",e.message.toString()) // If there are not enough questions this will happen
                _quiz.value = null
                _status.value = ApiStatus.ERROR
            }
        }
    }


    fun setupQuiz(authToken:String){
        coroutineScope.launch {
            var coursesDeferred = Network.instance.getAllCourses("Bearer $authToken")
            try {
                val coursesWithLessons = coursesDeferred.await().asDomainModel()
                _courses.value = coursesWithLessons
            }
            catch (e:Exception){
                Log.e("error",e.message.toString())
                _courses.value = ArrayList()
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
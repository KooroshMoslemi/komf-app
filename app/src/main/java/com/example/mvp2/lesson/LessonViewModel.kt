package com.example.mvp2.lesson

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvp2.domain.Lesson
import com.example.mvp2.domain.Vocab
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.network.Network
import com.example.mvp2.network.asDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class LessonViewModel(authToken: String, courseId: Long) : ViewModel(){

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val _lessons = MutableLiveData<List<Lesson>>()
    val lessons : LiveData<List<Lesson>> get() = _lessons

    private val _lessonVocabs = MutableLiveData<List<Vocab>>()
    val lessonVocabs : LiveData<List<Vocab>> get() = _lessonVocabs

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status


    var invalidateLessonFlag : Boolean = false


    private val _navigateToFlashCard = MutableLiveData<Boolean?>()
    val navigateToFlashCard: LiveData<Boolean?>
        get() = _navigateToFlashCard

    init {
        getLessons(authToken,courseId)
    }


    fun invalidateLessons(){
        _lessons.value = ArrayList()
        //_lessonVocabs.value = ArrayList()
    }


    fun invalidateLessonsOnReturn(){
        invalidateLessonFlag = true
    }


    fun getLessons(authToken:String, courseId:Long){
        coroutineScope.launch {
            var lessonsDeferred = Network.instance.getLessons("Bearer $authToken",courseId)
            try {
                //Todo: automatically navigate back to home when lessons are empty
                Log.e("LessonViewModel","lessons received")
                val results = lessonsDeferred.await().asDomainModel()
                _status.value = ApiStatus.DONE
                _lessons.value = results
            }
            catch (e:Exception){
                Log.e("error",e.message.toString())
                _status.value = ApiStatus.ERROR
                _lessons.value = ArrayList()
            }
        }
    }

    fun getVocabs(authToken:String, lessonId:Long){
        coroutineScope.launch {
            var vocabsDeferred = Network.instance.getVocabs("Bearer $authToken",lessonId)
            try {
                val results = vocabsDeferred.await().asDomainModel()
                Log.e("LessonViewModel","vocabsFetched ${results.size}")
                if(results.size > 0)
                {
                    _status.value = ApiStatus.DONE
                    _lessonVocabs.value = results
                    onFlashCardNavigating()
                }
                else{
                    Log.e("LessonViewModel","no vocab for this lesson in database")
                }
            }
            catch (e:Exception){
                Log.e("error",e.message.toString())
                _status.value = ApiStatus.ERROR
                _lessonVocabs.value = ArrayList()
            }
        }
    }


    fun doneFlashCardNavigating() {
        _navigateToFlashCard.value = null
    }

    fun onFlashCardNavigating(){
        _navigateToFlashCard.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
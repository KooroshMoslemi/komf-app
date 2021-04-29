package com.example.mvp2.lesson

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvp2.domain.Lesson
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

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    init {
        getLessons(authToken,courseId)
    }


    private fun getLessons(authToken:String, courseId:Long){
        coroutineScope.launch {
            var lessonsDeferred = Network.instance.getLessons("Bearer $authToken",courseId)
            try {
                //Todo: automatically navigate back to home when lessons are empty
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



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
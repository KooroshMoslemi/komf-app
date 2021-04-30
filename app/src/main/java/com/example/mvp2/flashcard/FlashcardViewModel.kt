package com.example.mvp2.flashcard

import android.util.Log
import androidx.lifecycle.*
import com.example.mvp2.database.DatabaseVocab
import com.example.mvp2.database.VocabDao
import com.example.mvp2.database.asDomainModel
import com.example.mvp2.domain.GeneralResponse
import com.example.mvp2.domain.Lesson
import com.example.mvp2.domain.ProgressRequest
import com.example.mvp2.domain.Vocab
import com.example.mvp2.lesson.LessonStatus
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.network.Network
import com.example.mvp2.network.asDomainModel
import kotlinx.coroutines.*
import java.lang.Exception

class FlashcardViewModel(private val vocabs: ArrayList<Vocab>,val lessonStatus: LessonStatus,val lessonId: Long, val database: VocabDao) : ViewModel() {


    private val _currentVocabIndex = MutableLiveData<Int>().apply {
        value = 0
    }

    var currentVocab : LiveData<Vocab> = Transformations.map(_currentVocabIndex){ vocabId->
        vocabs[vocabId!!]
    }

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    //val vocabIdsUpdate = ArrayList<Long>()


    private val _navigateToLesson = MutableLiveData<Boolean?>()
    val navigateToLesson: LiveData<Boolean?>
        get() = _navigateToLesson


    private var _flashcardSide = MutableLiveData<Boolean>(false)//0 front - 1 back
    val flashcardSide: LiveData<Boolean>
        get() = _flashcardSide


    init {

    }


    fun switchSide(){
        _flashcardSide.value = _flashcardSide.value?.not()
    }


//    private fun fetchVocabs(): LiveData<List<DatabaseVocab>> {
//            when(lesson.lessonStatus){
//                LessonStatus.COMPLETED->{
//                    return fetchVocabsCompletedMode(lesson.lessonId)
//                }
//                else->{//active
//                    return fetchVocabsInProgressMode(lesson.lessonId,lesson.remainedVocabIds)
//                }
//            }
//    }


//    private  fun fetchVocabsInProgressMode(lessonId:Long, vocabIds:List<Long>): LiveData<List<DatabaseVocab>> {
//        return database.getInProgressLessonVocabs(lessonId, vocabIds)
//    }
//
//    private fun fetchVocabsCompletedMode(lessonId:Long): LiveData<List<DatabaseVocab>> {
//        return database.getCompletedLessonVocabs(lessonId)
//    }


    fun nextVocab(){
        _flashcardSide.value = false
        _currentVocabIndex.value?.let { a->
            if(a+1 < vocabs.size)
                _currentVocabIndex.value = a+1
        }
    }

    fun prevVocab(){
        _flashcardSide.value = false
        _currentVocabIndex.value?.let { a->
            if(a-1 >= 0)
                _currentVocabIndex.value = a-1
        }
    }


    fun crossVocab(){
        _flashcardSide.value = false
        _currentVocabIndex.value?.let { a->
            if(a+1 < vocabs.size)
                _currentVocabIndex.value = a+1
            else{
                _navigateToLesson.value = true
            }
        }
    }


    fun checkVocab(authToken:String){
        _flashcardSide.value = false
        _currentVocabIndex.value?.let { a->
            if(a+1 < vocabs.size)
            {
                //.add(vocabs[a].lessonId)

                coroutineScope.launch {
                    var progressDeferred = Network.instance.submitVocabProgress(
                        "Bearer $authToken",
                        ProgressRequest(lessonId.toString(), vocabs[a].vocabId.toString())
                    )
                    try {
                        val results:GeneralResponse = progressDeferred.await()
                        if(results.status.contains("success")){
                            Log.e("FlashcardViewModel","progress submit succeeded")
                            _currentVocabIndex.value = a+1
                        }
                        else{
                            Log.e("FlashcardViewModel","progress submit failed")
                        }
                    } catch (e: Exception) {
                        Log.e("error", e.message.toString())
                    }
                }
            }
            else{
                _navigateToLesson.value = true
            }
        }
    }

}
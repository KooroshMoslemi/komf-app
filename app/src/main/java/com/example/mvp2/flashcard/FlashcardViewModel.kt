package com.example.mvp2.flashcard

import android.util.Log
import androidx.lifecycle.*
import com.example.mvp2.database.DatabaseVocab
import com.example.mvp2.database.VocabDao
import com.example.mvp2.database.asDomainModel
import com.example.mvp2.domain.Lesson
import com.example.mvp2.domain.Vocab
import com.example.mvp2.lesson.LessonStatus
import kotlinx.coroutines.*

class FlashcardViewModel(private val lesson: Lesson, val database: VocabDao) : ViewModel() {

    private val _vocabs :  LiveData<List<Vocab>> = Transformations.map(fetchVocabs()){
        it.asDomainModel()
    }

    private val _currentVocabIndex = MutableLiveData<Int>().apply {
        value = 0
    }

    var currentVocab : LiveData<Vocab> = Transformations.switchMap(_currentVocabIndex){ vocabId->
        Transformations.map(_vocabs){ vocabList->
            vocabList[vocabId!!]
        }
    }

    val lessonStatus: LessonStatus
        get() = lesson.lessonStatus


    val vocabIdsUpdate = ArrayList<Long>()


    private var _flashcardSide = MutableLiveData<Boolean>(false)//0 front - 1 back
    val flashcardSide: LiveData<Boolean>
        get() = _flashcardSide


    init {

    }


    fun switchSide(){
        _flashcardSide.value = _flashcardSide.value?.not()
    }


    private fun fetchVocabs(): LiveData<List<DatabaseVocab>> {
            when(lesson.lessonStatus){
                LessonStatus.COMPLETED->{
                    return fetchVocabsCompletedMode(lesson.lessonId)
                }
                else->{//active
                    return fetchVocabsInProgressMode(lesson.lessonId,lesson.remainedVocabIds)
                }
            }
    }


    private  fun fetchVocabsInProgressMode(lessonId:Long, vocabIds:List<Long>): LiveData<List<DatabaseVocab>> {
        return database.getInProgressLessonVocabs(lessonId, vocabIds)
    }

    private fun fetchVocabsCompletedMode(lessonId:Long): LiveData<List<DatabaseVocab>> {
        return database.getCompletedLessonVocabs(lessonId)
    }


    fun nextVocab(){
        _flashcardSide.value = false
        _currentVocabIndex.value?.let { a->
            if(a+1 < _vocabs.value!!.size)
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
            if(a+1 < _vocabs.value!!.size)
                _currentVocabIndex.value = a+1
            else{
                //Todo:save and exit question
            }
        }
    }


    fun checkVocab(){
        _flashcardSide.value = false
        _currentVocabIndex.value?.let { a->
            if(a+1 < _vocabs.value!!.size)
            {
                vocabIdsUpdate.add(_vocabs.value!![a].lessonId)
                _currentVocabIndex.value = a+1
            }
            else{
                //Todo:save and exit question
            }
        }
    }

}
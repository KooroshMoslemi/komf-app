package com.example.mvp2.quiz

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.mvp2.domain.Question
import com.example.mvp2.domain.Quiz
import com.example.mvp2.domain.Vocab
import java.util.*
import kotlin.collections.ArrayList

class QuizViewModel(private val quiz : Quiz) : ViewModel() {


    private val DONE = 0L

    private val ONE_SECOND = 1000L

    private val COUNTDOWN_TIME = quiz.time * 60000L

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private val timer: CountDownTimer

    var userAnswers = ArrayList<Int>(Collections.nCopies(quiz.questions.size,-1))

    private val _eventQuizFinish = MutableLiveData<Boolean>()
    val eventQuizFinish: LiveData<Boolean>
        get() = _eventQuizFinish


    private val _currentQuestionIndex = MutableLiveData<Int>().apply {
        value = 0
    }
    val currentQuestionIndex: LiveData<Int>
        get() = _currentQuestionIndex

    var currentQuestion : LiveData<Question> = Transformations.switchMap(_currentQuestionIndex){ questionId->
        MutableLiveData<Question>().apply {
            value = quiz.questions[questionId]
        }
    }


    init {

        Log.e("QuizViewModel",quiz.time.toString())

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished/ONE_SECOND
            }

            override fun onFinish() {
                onQuizFinish()
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    private fun Boolean.toInt() = if (this) 1 else 0

    fun calcScore(): Int {
        return userAnswers.mapIndexed { index, answer ->
            (answer == quiz.questions[index].correctChoice).toInt()
        }.sum()
    }

    fun onSubmit(checkedId : Int,forwardDirection:Boolean=true){
        _currentQuestionIndex.value?.let { idx ->
            userAnswers[idx] = checkedId
            if (forwardDirection)
                onNext()
            else
                onPrev()
        }
    }

    fun onNext(){
        _currentQuestionIndex.value?.let { a->
            if(a+1 < quiz.questions.size)
                _currentQuestionIndex.value = a+1
        }
    }

    fun onPrev(){
        _currentQuestionIndex.value?.let { a->
            if(a-1 >= 0)
                _currentQuestionIndex.value = a-1
        }
    }

    fun onQuizFinish() {
        timer.cancel() //Todo: remove this line - when we navigate onCleared will be triggered
        _currentTime.value = DONE
        _eventQuizFinish.value = true
    }

    fun onQuizFinishComplete() {
        _eventQuizFinish.value = false
    }

}
package com.example.mvp2.quiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvp2.R
import com.example.mvp2.databinding.FragmentQuizBinding
import com.example.mvp2.flashcard.FlashcardFragmentArgs
import com.example.mvp2.flashcard.FlashcardViewModel
import com.example.mvp2.flashcard.FlashcardViewModelFactory

class QuizFragment : Fragment() {

    lateinit var binding : FragmentQuizBinding
    lateinit var quizViewModel : QuizViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_quiz, container, false
        )

        val args = QuizFragmentArgs.fromBundle(requireArguments())
        //Log.e("QuizFragment","${args.quiz.time} ${args.quiz.questions.size}")
        val viewModelFactory = QuizViewModelFactory(args.quiz)
        quizViewModel = ViewModelProvider(this,viewModelFactory).get(QuizViewModel::class.java)

        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.quizViewModel = quizViewModel


        quizViewModel.eventQuizFinish.observe(viewLifecycleOwner, Observer { hasFinished->
            if (hasFinished){
                //Todo: navigate to quiz result
                Toast.makeText(this.activity,"Quiz Finished with score: ${quizViewModel.calcScore()}",Toast.LENGTH_LONG).show()
                quizViewModel.onQuizFinishComplete()
            }
        })


        quizViewModel.currentQuestionIndex.observe(viewLifecycleOwner, Observer { questionIndex->
            questionIndex?.let {
                when(quizViewModel.userAnswers[questionIndex]){
                    1 -> binding.firstAnswerRadioButton.isChecked = true
                    2 -> binding.secondAnswerRadioButton.isChecked = true
                    3 -> binding.thirdAnswerRadioButton.isChecked = true
                    4 -> binding.fourthAnswerRadioButton.isChecked = true
                    else -> binding.questionRadioGroup.clearCheck()
                }
            }
        })


        binding.submitButton.setOnClickListener {
            applyTransitionLogic()
        }

        binding.backButton.setOnClickListener {
            applyTransitionLogic(false)
        }

        return binding.root
    }

    fun applyTransitionLogic(next:Boolean = true){
        var answerIndex = when (binding.questionRadioGroup.checkedRadioButtonId) {
            R.id.firstAnswerRadioButton -> 1
            R.id.secondAnswerRadioButton -> 2
            R.id.thirdAnswerRadioButton -> 3
            R.id.fourthAnswerRadioButton -> 4
            else -> -1
        }

        quizViewModel.onSubmit(answerIndex,next)
    }
}
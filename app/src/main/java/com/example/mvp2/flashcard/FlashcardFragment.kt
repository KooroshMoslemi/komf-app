package com.example.mvp2.flashcard

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mvp2.R
import com.example.mvp2.database.SessionManager
import com.example.mvp2.database.getDatabase
import com.example.mvp2.databinding.FragmentFlashcardBinding
import kotlinx.android.synthetic.main.card_back.view.*
import kotlinx.android.synthetic.main.card_front.*
import kotlinx.android.synthetic.main.card_front.view.*
import java.util.*
import kotlin.collections.ArrayList


class FlashcardFragment : Fragment(),TextToSpeech.OnInitListener {

    private  lateinit var  mSetRightOut: AnimatorSet
    private lateinit var mSetLeftIn: AnimatorSet
    private var mIsBackVisible : Boolean = false
    private lateinit var binding: FragmentFlashcardBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_flashcard, container, false
        )

        val application = requireNotNull(this.activity).application
        sessionManager = SessionManager(context!!)
        val args = FlashcardFragmentArgs.fromBundle(requireArguments())
        //Toast.makeText(context,args.lesson.lessonTitle,Toast.LENGTH_LONG).show()
        val dataSource = getDatabase(application).vocabDao
        val viewModelFactory = FlashcardViewModelFactory(args.vocabs.vocabs,args.vocabs.lessonStatus,args.vocabs.lessonId, dataSource)
        val flashcardViewModel = ViewModelProvider(this, viewModelFactory).get(FlashcardViewModel::class.java)

        val tts = TextToSpeech(context, this)
        tts.language = Locale.US

        flashcardViewModel.currentVocab.observe(viewLifecycleOwner, Observer { vocab ->
            vocab.let {
                binding.frontInclude.vocabTv.text = vocab.word
                binding.backInclude.defTv.text = "def. ${vocab.def}"
                binding.backInclude.synTv.text = "syn. ${vocab.syn}"
                binding.backInclude.ex1Tv.text = "e.g. ${vocab.ex1}"
                binding.backInclude.ex2Tv.text = "e.g. ${vocab.ex2}"
            }
        })

        flashcardViewModel.navigateToLesson.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })

        binding.checkVocabBtn.setOnClickListener {
            flashcardViewModel.checkVocab(sessionManager.fetchAuthToken()!!)
        }


        binding.frontInclude.ttsImageView.setOnClickListener {
            tts.speak(binding.frontInclude.vocabTv.text.toString(), TextToSpeech.QUEUE_ADD, null)
        }

        flashcardViewModel.flashcardSide.observe(viewLifecycleOwner, Observer { side ->
            side?.let {
                //mIsBackVisible = side.not()
                Log.e("side", "${side},${mIsBackVisible}")
                if (side != mIsBackVisible)
                    flipCard(binding.flashcard)
            }
        })

        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.flashcardViewModel = flashcardViewModel


        loadAnimations()
        changeCameraDistance()



        return binding.root
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        binding.cardFront.cameraDistance = scale
        binding.cardBack.cameraDistance = scale
    }

    private fun loadAnimations() {
        mSetRightOut = AnimatorInflater.loadAnimator(this.activity, R.animator.out_animation) as AnimatorSet
        mSetLeftIn = AnimatorInflater.loadAnimator(this.activity, R.animator.in_animation) as AnimatorSet
    }

    fun flipCard(view: View?) {
        mIsBackVisible = if (!mIsBackVisible) {
            mSetRightOut.setTarget(binding.cardFront)
            mSetLeftIn.setTarget(binding.cardBack)
            mSetRightOut.start()
            mSetLeftIn.start()
            true
        } else {
            mSetRightOut.setTarget(binding.cardBack)
            mSetLeftIn.setTarget(binding.cardFront)
            mSetRightOut.start()
            mSetLeftIn.start()
            false
        }
    }

    override fun onInit(p0: Int) {

    }
}
package com.example.mvp2.lesson

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvp2.R
import com.example.mvp2.database.SessionManager
import com.example.mvp2.databinding.FragmentLessonBinding
import com.example.mvp2.domain.Vocab
import com.example.mvp2.domain.VocabList
import com.example.mvp2.flashcard.FlashcardFragmentArgs
import com.example.mvp2.home.HomeViewModel
import com.example.mvp2.home.HomeViewModelFactory
import com.example.mvp2.utils.hideBottomNavigationView
import com.example.mvp2.utils.showBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView


class LessonFragment : Fragment() {


    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: LessonAdapter
    private lateinit var viewModel: LessonViewModel
    private lateinit var sessionManager: SessionManager
    private lateinit var binding : FragmentLessonBinding
    private var selectedLessonStatus : LessonStatus = LessonStatus.INACTIVE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lesson, container, false)



        val args = LessonFragmentArgs.fromBundle(requireArguments())
        sessionManager = SessionManager(context!!)
        val viewModelFactory = LessonViewModelFactory(sessionManager.fetchAuthToken()!!,args.courseId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LessonViewModel::class.java)


        Log.e("LessonFragment","here")
        if(viewModel.invalidateLessonFlag){
            viewModel.invalidateLessons()
        }
        startShimmerEffect()


        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel


        mLayoutManager = LinearLayoutManager(this.activity, RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = mLayoutManager


        viewModel.lessons.observe(viewLifecycleOwner, Observer{ lessons->
            if (lessons.isNotEmpty())
            {
                mAdapter = LessonAdapter(lessons,LessonAdapter.OnClickListener{
                    Log.e("LessonFragment","lesson item clicked!")
                    selectedLessonStatus = it.lessonStatus
                    viewModel.getVocabs(sessionManager.fetchAuthToken()!!,it.lessonId)
                })
                stopShimmerEffect()
            }
            else{
                Log.e("LessonFragment","getting lessons")
                viewModel.getLessons(sessionManager.fetchAuthToken()!!,args.courseId)
            }
        })


        viewModel.navigateToFlashCard.observe(viewLifecycleOwner, Observer {
            viewModel.lessonVocabs.value?.let { vocabs->
                if(selectedLessonStatus != LessonStatus.INACTIVE){ //!vocabs.isNullOrEmpty() && //Todo: check this if again
                    viewModel.invalidateLessonsOnReturn()
                    findNavController().navigate(
                            LessonFragmentDirections.actionLessonFragmentToFlashcardFragment(
                                    VocabList(
                                            vocabs as ArrayList<Vocab>,
                                            selectedLessonStatus)))
                    viewModel.doneFlashCardNavigating()
                }
            }
        })

        val bottomView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        hideBottomNavigationView(bottomView)


        return binding.root
    }

    private fun stopShimmerEffect(){
        Log.e("LessonFragment","stopShimmer")
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.adapter = mAdapter
    }

    private fun startShimmerEffect(){
        Log.e("LessonFragment","startShimmer")
        binding.shimmerViewContainer.startShimmer()
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }
}
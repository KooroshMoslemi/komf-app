package com.example.mvp2.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvp2.R
import com.example.mvp2.database.SessionManager
import com.example.mvp2.databinding.FragmentLessonBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val binding: FragmentLessonBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_lesson, container, false)


        val args = LessonFragmentArgs.fromBundle(requireArguments())
        sessionManager = SessionManager(context!!)
        val viewModelFactory = LessonViewModelFactory(sessionManager.fetchAuthToken()!!,args.courseId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LessonViewModel::class.java)


        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel


        mLayoutManager = LinearLayoutManager(this.activity, RecyclerView.VERTICAL, false)
        binding.recyclerView.layoutManager = mLayoutManager


        viewModel.lessons.observe(viewLifecycleOwner, Observer{ lessons->
            if (lessons.isNotEmpty())
            {
                mAdapter = LessonAdapter(lessons)
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.recyclerView.adapter = mAdapter
            }
        })

        val bottomView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        hideBottomNavigationView(bottomView)


        return binding.root
    }
}
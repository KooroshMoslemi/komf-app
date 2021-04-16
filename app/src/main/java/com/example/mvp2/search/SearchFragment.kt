package com.example.mvp2.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.mvp2.R
import com.example.mvp2.databinding.FragmentProgressBinding
import androidx.navigation.fragment.findNavController
import com.example.mvp2.course.CourseEnrollmentBottomSheet
import com.example.mvp2.database.SessionManager
import com.example.mvp2.databinding.FragmentSearchBinding
import com.example.mvp2.login.LoginViewModel
import com.example.mvp2.quiz.QuizViewModel
import com.example.mvp2.utils.hideBottomNavigationView
import com.example.mvp2.utils.showBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchFragment : Fragment() {


    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_search, container, false)


        val bottomView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        showBottomNavigationView(bottomView)

        sessionManager = SessionManager(context!!)
        val viewModelFactory = SearchViewModelFactory(sessionManager.fetchAuthToken()!!)
        val viewModel = ViewModelProvider(this,viewModelFactory).get(SearchViewModel::class.java)
        val coursesAdapter = SearchCourseAdapter(SearchCourseAdapter.OnClickListener{ course->

            CourseEnrollmentBottomSheet.showDialog(fragmentManager!!,course, object : CourseEnrollmentBottomSheet.Callbacks{
                override fun onStateChanged(courseId: Long) {
                    Log.e("SearchFragment",courseId.toString())
                    viewModel.enrollCourse(sessionManager.fetchAuthToken()!!,courseId)
                }
            })

        })

        binding.edtSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(query: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(query: CharSequence?, start: Int, count: Int, after: Int) {
                coursesAdapter.filter.filter(query)
            }

            override fun afterTextChanged(query: Editable?) {
            }

        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.coursesList.adapter = coursesAdapter

        return binding.root
    }
}
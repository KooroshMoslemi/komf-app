package com.example.mvp2.home

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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.mvp2.R
import com.example.mvp2.course.CourseEnrollmentBottomSheet
import com.example.mvp2.course.CourseViewPagerAdapter
import com.example.mvp2.database.SessionManager
import com.example.mvp2.databinding.FragmentHomeBinding
import com.example.mvp2.domain.Course
import com.example.mvp2.domain.Vocab
import com.example.mvp2.flashcard.FlashcardFragmentArgs
import com.example.mvp2.flashcard.FlashcardViewModel
import com.example.mvp2.flashcard.FlashcardViewModelFactory
import com.example.mvp2.utils.HorizontalMarginItemDecoration
import com.example.mvp2.utils.showBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.math.abs

class HomeFragment : Fragment() {

//    private val sessionManager = SessionManager(context!!)
//
//    private val viewModel: HomeViewModel by lazy {
//        val activity = requireNotNull(this.activity) {
//            "You can only access the viewModel after onActivityCreated()"
//        }
//        ViewModelProviders.of(this, HomeViewModel.Factory(activity.application,sessionManager.fetchAuthToken()!!))
//                .get(HomeViewModel::class.java)
//    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var coursePagerAdapter : CourseViewPagerAdapter
    private lateinit var viewModel : HomeViewModel
    private lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        sessionManager = SessionManager(context!!)
        val viewModelFactory = HomeViewModelFactory(application, sessionManager.fetchAuthToken()!!)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel = viewModel



        binding.lessonCard.setOnClickListener {
            if(viewModel.vocabs.value?.isNotEmpty() == true)
                viewModel.onLessonNavigating()
            else
                Toast.makeText(context, "Empty Vocab", Toast.LENGTH_LONG).show()
        }


        viewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            Observer<Boolean> { isNetworkError ->
                if (isNetworkError) onNetworkError()
            })

        viewModel.navigateToLesson.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToLessonFragment())
                viewModel.doneLessonNavigating()
            }
        })


        val bottomView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        showBottomNavigationView(bottomView)

        coursePagerAdapter = CourseViewPagerAdapter(CourseViewPagerAdapter.OnClickListener{course->
            Toast.makeText(activity,course.courseId.toString(),Toast.LENGTH_SHORT).show()
            //Todo: Navigate To Course Lessons
        })

        setupViewpager(coursePagerAdapter)


        viewModel.courses.observe(viewLifecycleOwner, Observer {
            it.let {
                Log.e("HomeFragment","popular courses updated!")
                coursePagerAdapter.submitList(it)
            }
        })


        return binding.root
    }

    private fun setupViewpager(adapter : CourseViewPagerAdapter) {
        binding.viewPager.adapter = adapter
        // You need to retain one page on each side so that the next and previous items are visible
        binding.viewPager.offscreenPageLimit = 1
        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = 100//26
        val currentItemHorizontalMarginPx = 200//42
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - 0.15f * abs(position)
        }
        binding.viewPager.setPageTransformer(pageTransformer)
        binding.viewPager.addItemDecoration(HorizontalMarginItemDecoration())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.vocabs.observe(viewLifecycleOwner, Observer<List<Vocab>> { vocabs ->
            vocabs?.apply {
                //Toast.makeText(context,vocabs.size.toString(),Toast.LENGTH_LONG).show()
                Log.e("vocab_size", vocabs.size.toString())
            }
        })
    }


    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }


}
package com.example.mvp2.setup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.mvp2.R
import com.example.mvp2.database.SessionManager
import com.example.mvp2.databinding.FragmentSetupBinding
import com.example.mvp2.domain.Course
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.utils.hideBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup
import com.nex3z.togglebuttongroup.button.LabelToggle
import kotlinx.android.synthetic.main.fragment_setup.view.*


class SetupFragment : Fragment() {

    private val viewModel: SetupViewModel by lazy {
        ViewModelProviders.of(this).get(SetupViewModel::class.java)
    }

    private lateinit var sessionManager: SessionManager
    private lateinit var loadingDialog: SweetAlertDialog
    private lateinit var errorDialog: SweetAlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentSetupBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_setup, container, false
        )
        binding.setLifecycleOwner(this)
        //binding.viewModel = viewModel

        sessionManager = SessionManager(context!!)

        loadingDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        loadingDialog.setCancelable(false)
        loadingDialog.titleText = "Loading"
        loadingDialog.show()

        errorDialog = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        errorDialog.setCancelable(false)
        errorDialog.setConfirmClickListener {
            it.dismiss()
        }
        errorDialog.titleText = "Choose at least one lesson"


        viewModel.setupQuiz(sessionManager.fetchAuthToken()!!)

        binding.buttonApply.setOnClickListener {
            val checkedIds : Set<Int> = binding.groupToggls.checkedIds
            val limit : Int = binding.spinnerLineType.selectedItem.toString().toInt()
            if(checkedIds.size > 0)
            {
                binding.buttonApply.startAnimation()
                viewModel.prepareQuiz(
                        sessionManager.fetchAuthToken()!!,
                        limit,
                        checkedIds.joinToString(",")
                )
            }
            else{
                errorDialog.show()
            }

//            for(id in checkedIds){
//                val chip : LabelToggle = binding.groupToggls.findViewById(id)
//                Log.e("SetupFragment",chip.text.toString())
//            }

        }



        viewModel.status.observe(viewLifecycleOwner, Observer { status ->
            if (status == ApiStatus.DONE) {
                Log.e("SetupFragment", "done")
                viewModel.onLessonNavigating()
            } else {
                Log.e("SetupFragment", "revert")
                binding.buttonApply.revertAnimation()
            }
        })


        viewModel.navigateToQuiz.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.doneLessonNavigating()
                findNavController().navigate(SetupFragmentDirections.actionSetupFragmentToQuizFragment(viewModel.quiz.value!!))
            }
        })


        viewModel.courses.observe(viewLifecycleOwner, Observer { course ->
            course?.let {
                loadingDialog.dismiss()
                val adapter: ArrayAdapter<Course> = ArrayAdapter(
                        context!!,
                        android.R.layout.simple_spinner_item,
                        course
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.courseSpinnerLineType.adapter = adapter
            }
        })


        viewModel.selectedCourse.observe(viewLifecycleOwner, Observer { course->
            binding.groupToggls.removeAllViews()
            course.lessons?.let { lessons->
                for(lesson in lessons){
                    val chip = inflater.inflate(R.layout.layout_chip_choice, binding.groupToggls, false) as LabelToggle
                    chip.text = lesson.lessonTitle
                    chip.id = lesson.lessonId.toInt()
                    binding.groupToggls.addView(chip)
                }
            }
        })


        binding.courseSpinnerLineType.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("SetupFragment","item selected!")
                viewModel.selectCourse(parent?.selectedItem as Course)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        })


        val bottomView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        hideBottomNavigationView(bottomView)




//        for (i in 1..30){
//            val chip = inflater.inflate(R.layout.layout_chip_choice,binding.tags,false) as ThemedButton
//            if(i<10){
//                chip.text = " ${i}"
//            }
//            else{
//                chip.text = "${i}"
//            }
//
//            chip.setOnClickListener {
//                binding.tags.selectButtonWithAnimation(chip)
//
//            }
//
//            binding.tags.addView(chip)
//        }



        return  binding.root
    }
}
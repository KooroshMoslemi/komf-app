package com.example.mvp2.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.example.mvp2.R
import com.example.mvp2.domain.Course
import com.example.mvp2.utils.RoundedCornerBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_description.*
import kotlinx.android.synthetic.main.fragment_setup.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class CourseEnrollmentBottomSheet : RoundedCornerBottomSheet() {


    interface Callbacks {
        fun onStateChanged(courseId: Long)
    }

    companion object {

        private const val COURSE_BUNDLE = "COURSE_BUNDLE"
        private var _unrollMode : Boolean = false

        fun showDialog(fragmentManager: FragmentManager, course: Course, callbacks: Callbacks , unrollMode : Boolean = false) {
            val dialog = CourseEnrollmentBottomSheet()
            _unrollMode = unrollMode
            dialog.arguments = bundleOf(
                    COURSE_BUNDLE to course
            )
            dialog.setCallback(callbacks)
            dialog.show(fragmentManager, "[COURSE_ENROLLMENT_BOTTOM_SHEET]")
        }
    }


    private var mCallbacks: Callbacks? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null


    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val bottomSheet = dialog!!.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = 800//ViewGroup.LayoutParams.MATCH_PARENT
        }

        view?.post {
            val parent = view?.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            mBottomSheetBehavior = behavior as BottomSheetBehavior<*>?
            mBottomSheetBehavior?.peekHeight = view?.measuredHeight!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity,R.style.Theme_MVP0)
        return inflater.cloneInContext(contextThemeWrapper).inflate(R.layout.bottom_sheet_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image_toggle.setOnClickListener { dismiss() }

        val course = (arguments!!.getParcelable<Course>(COURSE_BUNDLE))
        course_title.text = course?.courseTitle
        course_description.text = course?.courseDescription

        if(_unrollMode)
            button_enroll.text = "Unroll"

        button_enroll.setOnClickListener {
            mCallbacks?.onStateChanged(course!!.courseId)
            dismiss()
        }
    }

    private fun setCallback(callbacks: Callbacks) {
        mCallbacks = callbacks
    }
}
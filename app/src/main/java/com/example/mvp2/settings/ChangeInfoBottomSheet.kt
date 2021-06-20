package com.example.mvp2.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.example.mvp2.R
import com.example.mvp2.domain.UserInfo
import com.example.mvp2.utils.RoundedCornerBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_change_info.*
import kotlinx.android.synthetic.main.bottom_sheet_change_pass.button_change
import kotlinx.android.synthetic.main.bottom_sheet_description.image_toggle

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChangeInfoBottomSheet : RoundedCornerBottomSheet() {


    interface Callbacks {
        fun onResponse(newFirstName: String,newLastName: String, newPhone: String)
    }

    companion object {

        private const val USER_BUNDLE = "USER_BUNDLE"

        fun showDialog(fragmentManager: FragmentManager, userInfo: UserInfo, callbacks: Callbacks) {
            val dialog = ChangeInfoBottomSheet()
            dialog.arguments = bundleOf(
                    ChangeInfoBottomSheet.USER_BUNDLE to userInfo
            )
            dialog.setCallback(callbacks)
            dialog.show(fragmentManager, "[CHANGE_INFO_BOTTOM_SHEET]")
        }
    }


    private var mCallbacks: Callbacks? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null


    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val bottomSheet = dialog!!.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = 900//ViewGroup.LayoutParams.MATCH_PARENT
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
        return inflater.cloneInContext(contextThemeWrapper).inflate(R.layout.bottom_sheet_change_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val info = (arguments!!.getParcelable<UserInfo>(ChangeInfoBottomSheet.USER_BUNDLE))
        fnameEd.setText(info?.userFirstName)
        lnameEd.setText(info?.userLastName)
        phoneEd.setText(info?.userPhone)

        image_toggle.setOnClickListener { dismiss() }

        button_change.setOnClickListener {
            mCallbacks?.onResponse(fnameEd.text.toString(),lnameEd.text.toString(),phoneEd.text.toString())
            dismiss()
        }
    }

    private fun setCallback(callbacks: Callbacks) {
        mCallbacks = callbacks
    }
}
package com.example.mvp2.utils

import android.view.View
import androidx.core.content.ContextCompat
import com.example.mvp2.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class RoundedCornerBottomSheet: BottomSheetDialogFragment() {

    override fun onStart() {
        super.onStart()

        view?.post {
            val parent = view?.parent as View
            parent.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.bg_bottom_sheet))
        }

    }

}
package com.example.mvp2.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mvp2.R
import com.example.mvp2.databinding.FragmentSettingsBinding
import com.example.mvp2.databinding.FragmentSetupBinding
import com.example.mvp2.utils.showBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentSettingsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_settings, container, false
        )


        val bottomView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        showBottomNavigationView(bottomView)

        return binding.root
    }
}
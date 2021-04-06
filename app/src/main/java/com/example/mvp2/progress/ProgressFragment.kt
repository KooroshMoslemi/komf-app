package com.example.mvp2.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mvp2.R
import com.example.mvp2.databinding.FragmentProgressBinding
import androidx.navigation.fragment.findNavController
import com.example.mvp2.utils.hideBottomNavigationView
import com.example.mvp2.utils.showBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProgressFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentProgressBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_progress, container, false)


        val bottomView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        showBottomNavigationView(bottomView)

        binding.quizBtn.setOnClickListener {
            findNavController().navigate(ProgressFragmentDirections.actionProgressFragmentToSetupFragment())
        }


        return binding.root
    }
}
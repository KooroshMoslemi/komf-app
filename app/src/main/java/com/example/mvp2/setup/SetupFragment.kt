package com.example.mvp2.setup

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.mvp2.R
import com.example.mvp2.databinding.FragmentSetupBinding
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.utils.hideBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nex3z.togglebuttongroup.button.LabelToggle
import kotlinx.android.synthetic.main.fragment_setup.view.*


class SetupFragment : Fragment() {

    private val viewModel: SetupViewModel by lazy {
        ViewModelProviders.of(this).get(SetupViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding : FragmentSetupBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_setup, container, false
        )
        binding.setLifecycleOwner(this)
        //binding.viewModel = viewModel

        binding.buttonApply.setOnClickListener {
            binding.buttonApply.startAnimation()
            viewModel.prepareQuiz()
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


        for(i in 1..30){
            val chip = inflater.inflate(R.layout.layout_chip_choice, binding.groupToggls, false) as LabelToggle
            chip.text = "${i}"
            binding.groupToggls.addView(chip)
        }



        return  binding.root
    }
}
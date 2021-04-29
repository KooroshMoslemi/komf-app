package com.example.mvp2.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.mvp2.R
import com.example.mvp2.database.SessionManager
import com.example.mvp2.databinding.FragmentSettingsBinding
import com.example.mvp2.databinding.FragmentSetupBinding
import com.example.mvp2.home.HomeFragmentDirections
import com.example.mvp2.home.HomeViewModel
import com.example.mvp2.lesson.LessonViewModel
import com.example.mvp2.utils.showBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsFragment : Fragment() {


    private val viewModel: SettingsViewModel by lazy {
        ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }
    private lateinit var sessionManager: SessionManager

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

        sessionManager = SessionManager(context!!)


        viewModel.navigateToLogin.observe(viewLifecycleOwner, Observer {
            it.let {
                if(it == true){
                    if(sessionManager.removeAuthToken() && sessionManager.removeFNameToken()){
                        this.findNavController()
                                .navigate(SettingsFragmentDirections.actionSettingsFragmentToLoginFragment())
                        viewModel.doneLoginNavigating()
                    }
                }
            }
        })


        binding.tvLogout.setOnClickListener {
            viewModel.logout(sessionManager.fetchAuthToken()!!)
        }



        return binding.root
    }
}
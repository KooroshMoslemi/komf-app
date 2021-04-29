package com.example.mvp2.login

import android.os.Bundle
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
import com.example.mvp2.database.SessionManager
import com.example.mvp2.databinding.FragmentLoginBinding
import com.example.mvp2.setup.SetupViewModel
import com.example.mvp2.utils.doIfCurrentDestination
import com.example.mvp2.utils.hideBottomNavigationView
import com.example.mvp2.utils.showBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentLoginBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        val bottomView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        hideBottomNavigationView(bottomView)

        sessionManager = SessionManager(context!!)

        sessionManager.fetchAuthToken().let {
            if(!it.isNullOrEmpty()) viewModel.navigationPolicy(it)
        }



        binding.tvRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }


        binding.cirLoginButton.setOnClickListener {
            viewModel.Login(
                    binding.editTextEmail.text.toString(),
                    binding.editTextPassword.text.toString()
            )
        }


       viewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
           it.let {
              navigateToHome(sessionManager.fetchAuthToken()!!)
           }
       })


        viewModel.login.observe(viewLifecycleOwner, Observer { loginResponse->
            loginResponse.let {
                sessionManager.saveAuthToken(loginResponse.authToken)

                Log.e("LoginFragment",loginResponse.authToken)
                navigateToHome(loginResponse.authToken)
            }

        })


        return binding.root
    }

    private fun navigateToHome(authToken:String){
//        findNavController().doIfCurrentDestination(R.id.homeFragment) {
//            Log.e("navigateToHome","navigating...")
//            navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(authToken))
//        }
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        viewModel.doneHomeNavigating()
    }
}
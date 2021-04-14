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
import com.example.mvp2.utils.hideBottomNavigationView
import com.example.mvp2.utils.showBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView

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

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }


        binding.cirLoginButton.setOnClickListener {
            viewModel.Login(
                    binding.editTextEmail.text.toString(),
                    binding.editTextPassword.text.toString()
            )
        }


        viewModel.login.observe(viewLifecycleOwner, Observer { loginResponse->
            loginResponse.let {
                sessionManager.saveAuthToken(loginResponse.authToken)

                Log.e("LoginFragment",sessionManager.fetchAuthToken()!!)
                //Todo: Navigate To Home
            }

        })


        return binding.root
    }
}
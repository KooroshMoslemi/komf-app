package com.example.mvp2.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.mvp2.R
import com.example.mvp2.databinding.FragmentRegisterBinding
import com.example.mvp2.login.LoginViewModel
import com.example.mvp2.utils.hideBottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView


class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentRegisterBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false
        )

        binding.tvLogin.setOnClickListener {
            navigateToLoginScreen()
        }

        binding.cirRegisterButton.setOnClickListener {
            //Todo: circular Button features + check inputs + sweet alert on failure
            viewModel.Register(
                    binding.editTextFName.text.toString(),
                    binding.editTextLName.text.toString(),
                    binding.editTextEmail.text.toString(),
                    binding.editTextMobile.text.toString(),
                    binding.editTextPassword.text.toString()
            )

        }


        viewModel.register.observe(viewLifecycleOwner, Observer { registerResponse->
            registerResponse.let {
                navigateToLoginScreen()
            }
        })


        val bottomView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        hideBottomNavigationView(bottomView)

        return binding.root
    }

    private fun navigateToLoginScreen(){
        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
    }
}
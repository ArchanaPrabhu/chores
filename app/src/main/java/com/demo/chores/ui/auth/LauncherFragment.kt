package com.demo.chores.ui.auth


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.demo.chores.R
import com.demo.chores.databinding.FragmentLauncherBinding
import com.demo.chores.di.auth.AuthScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class LauncherFragment @Inject constructor() : Fragment() {
    private var _binding: FragmentLauncherBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: AuthViewModel by viewModels{
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLauncherBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.register.setOnClickListener {
            navRegistration()
        }


        binding.login.setOnClickListener {
            navLogin()
        }

        binding.forgotPassword.setOnClickListener {
            navForgotPassword()
        }

        binding.focusableView.requestFocus() // reset focus

    }

    fun navLogin(){
        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    fun navRegistration(){
        findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)
    }

    fun navForgotPassword(){
        findNavController().navigate(R.id.action_launcherFragment_to_forgotPasswordFragment)
    }
}






















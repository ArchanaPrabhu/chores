package com.demo.chores.ui.auth


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.demo.chores.BaseApplication

import com.demo.chores.R
import com.demo.chores.databinding.FragmentLoginBinding
import com.demo.chores.di.auth.AuthScope
import com.demo.chores.fragments.auth.AuthFragmentFactory
import com.demo.chores.ui.UICommunicationListener
import com.demo.chores.ui.auth.state.AuthStateEvent
import com.demo.chores.ui.auth.state.LoginFields
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class LoginFragment @Inject constructor() :
    Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val TAG: String = "AppDebug"

    lateinit var uiCommunicationListener: UICommunicationListener

    var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val viewModel: AuthViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as BaseApplication).authComponent()
            .inject(this)
        super.onCreate(savedInstanceState)
        viewModel.cancelActiveJobs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        binding.loginButton.setOnClickListener(View.OnClickListener {
            login()
        })
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.loginFields?.let {
                it.login_email?.let {
                    _binding?.inputEmail?.setText(it)
                }
                it.login_password?.let {
                    _binding?.inputPassword?.setText(it)
                }
            }
        })
    }

    fun login() {
        viewModel.setStateEvent(
            AuthStateEvent.LoginAttemptEvent(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement UICommunicationListener")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(
            LoginFields(
                _binding?.inputEmail?.text.toString(),
                _binding?.inputPassword?.text.toString()
            )
        )
    }
}

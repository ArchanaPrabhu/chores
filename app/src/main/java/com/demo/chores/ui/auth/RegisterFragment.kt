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
import com.demo.chores.R
import com.demo.chores.databinding.FragmentRegisterBinding
import com.demo.chores.di.auth.AuthScope
import com.demo.chores.ui.UICommunicationListener
import com.demo.chores.ui.auth.state.AuthStateEvent
import com.demo.chores.ui.auth.state.RegistrationFields
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AuthScope
class RegisterFragment
@Inject
constructor(
) : Fragment(R.layout.fragment_register) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val TAG: String = "AppDebug"

    lateinit var uiCommunicationListener: UICommunicationListener

    val viewModel: AuthViewModel by viewModels {
        viewModelFactory
    }

    var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.cancelActiveJobs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            register()
        }
        subscribeObservers()
    }

    fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.registrationFields?.let {
                it.registration_email?.let { binding.inputEmail.setText(it) }
                it.registration_username?.let { binding.inputUsername.setText(it) }
                it.registration_password?.let { binding.inputPassword.setText(it) }
                it.registration_confirm_password?.let { binding.inputPasswordConfirm.setText(it) }
            }
        })
    }

    fun register() {
        viewModel.setStateEvent(
            AuthStateEvent.RegisterAttemptEvent(
                binding.inputEmail.text.toString(),
                binding.inputUsername.text.toString(),
                binding.inputPassword.text.toString(),
                binding.inputPasswordConfirm.text.toString()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                binding.inputEmail.text.toString(),
                binding.inputUsername.text.toString(),
                binding.inputPassword.text.toString(),
                binding.inputPasswordConfirm.text.toString()
            )
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            uiCommunicationListener = context as UICommunicationListener
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement UICommunicationListener")
        }
    }
}


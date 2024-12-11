package com.demo.chores.fragments.auth

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.demo.chores.ui.auth.ForgotPasswordFragment
import com.demo.chores.ui.auth.LauncherFragment
import com.demo.chores.ui.auth.LoginFragment
import com.demo.chores.ui.auth.RegisterFragment
import com.demo.chores.di.auth.AuthScope
import javax.inject.Inject

@AuthScope
class AuthFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            LauncherFragment::class.java.name -> {
                LauncherFragment()
            }

            LoginFragment::class.java.name -> {
                LoginFragment()
            }

            RegisterFragment::class.java.name -> {
                RegisterFragment()
            }

            ForgotPasswordFragment::class.java.name -> {
                ForgotPasswordFragment(viewModelFactory)
            }

            else -> {
                LauncherFragment()
            }
        }


}
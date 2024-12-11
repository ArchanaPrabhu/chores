package com.demo.chores.di.auth

import com.demo.chores.ui.auth.AuthActivity
import com.demo.chores.ui.auth.LoginFragment
import dagger.Subcomponent

@AuthScope
@Subcomponent(
    modules = [
        AuthModule::class,
        AuthViewModelModule::class,
        AuthFragmentsModule::class
    ])
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory{

        fun create(): AuthComponent
    }

    fun inject(authActivity: AuthActivity)

    fun inject(loginFragment: LoginFragment)

}

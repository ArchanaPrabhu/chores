package com.demo.chores.di.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.chores.di.auth.keys.AuthViewModelKey
import com.demo.chores.ui.auth.AuthViewModel
import com.demo.chores.viewmodels.AuthViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @AuthScope
    @Binds
    abstract fun bindViewModelFactory(factory: AuthViewModelFactory): ViewModelProvider.Factory

    @AuthScope
    @Binds
    @IntoMap
    @AuthViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

}
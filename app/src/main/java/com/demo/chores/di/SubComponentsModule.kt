package com.demo.chores.di

import com.demo.chores.di.auth.AuthComponent
import com.demo.chores.di.main.MainComponent
import dagger.Module

@Module(
    subcomponents = [
        AuthComponent::class,
        MainComponent::class
    ]
)
class SubComponentsModule
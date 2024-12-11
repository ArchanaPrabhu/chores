package com.demo.chores.di

import android.app.Application
import com.demo.chores.di.auth.AuthComponent
import com.demo.chores.di.main.MainComponent
import com.demo.chores.session.SessionManager
import com.demo.chores.ui.BaseActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent {

    val sessionManager: SessionManager

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(baseActivity: BaseActivity)

    fun authComponent(): AuthComponent.Factory

    fun mainComponent(): MainComponent.Factory
}
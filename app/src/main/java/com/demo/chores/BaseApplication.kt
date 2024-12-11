package com.demo.chores

import android.app.Application
import com.demo.chores.di.AppComponent
import com.demo.chores.di.DaggerAppComponent
import com.demo.chores.di.auth.AuthComponent
import com.demo.chores.di.main.MainComponent

class BaseApplication : Application() {

    lateinit var appComponent: AppComponent

    private var authComponent: AuthComponent? = null

    private var mainComponent: MainComponent? = null

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    fun releaseMainComponent(){
        mainComponent = null
    }

    fun mainComponent(): MainComponent {
        if(mainComponent == null){
            mainComponent = appComponent.mainComponent().create()
        }
        return mainComponent as MainComponent
    }

    fun releaseAuthComponent(){
        authComponent = null
    }

    fun authComponent(): AuthComponent {
        if(authComponent == null){
            authComponent = appComponent.authComponent().create()
        }
        return authComponent as AuthComponent
    }

    fun initAppComponent(){
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }


}
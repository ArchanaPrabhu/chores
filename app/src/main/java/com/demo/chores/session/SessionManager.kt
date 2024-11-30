package com.demo.chores.session

import android.app.Application
import com.demo.chores.persistence.AuthTokenDao

class SessionManager(
    val authTokenDao: AuthTokenDao,
    val application: Application
)
package com.demo.chores.repository.auth

import com.demo.chores.api.auth.OpenApiAuthService
import com.demo.chores.persistence.AccountPropertiesDao
import com.demo.chores.persistence.AuthTokenDao
import com.demo.chores.session.SessionManager

class AuthRepository
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
)
{

}
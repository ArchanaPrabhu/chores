package com.demo.chores.repository.main

import com.demo.chores.di.main.MainScope
import com.demo.chores.models.AccountProperties
import com.demo.chores.models.AuthToken
import com.demo.chores.ui.main.account.state.AccountViewState
import com.demo.chores.util.DataState
import com.demo.chores.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@MainScope
interface AccountRepository {

    fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>

    fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>>
}
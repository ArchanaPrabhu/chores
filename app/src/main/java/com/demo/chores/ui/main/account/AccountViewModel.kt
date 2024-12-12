package com.demo.chores.ui.main.account

import android.util.Log
import com.demo.chores.di.main.MainScope
import com.demo.chores.models.AccountProperties
import com.demo.chores.repository.main.AccountRepositoryImpl
import com.demo.chores.session.SessionManager
import com.demo.chores.ui.BaseViewModel
import com.demo.chores.ui.main.account.state.AccountStateEvent.*
import com.demo.chores.ui.main.account.state.AccountViewState
import com.demo.chores.util.*
import com.demo.chores.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@MainScope
class AccountViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    val accountRepository: AccountRepositoryImpl
) : BaseViewModel<AccountViewState>() {

    override fun handleNewData(stateEvent: StateEvent?, data: AccountViewState) {

        Log.d(TAG, "handleNewData: ${data}")

        data.accountProperties?.let { accountProperties ->
            setAccountPropertiesData(accountProperties)
        }

        _activeStateEventTracker.removeStateEvent(stateEvent)
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        sessionManager.cachedToken.value?.let { authToken ->
            val job: Flow<DataState<AccountViewState>> = when (stateEvent) {
                is GetAccountPropertiesEvent -> {
                    accountRepository.getAccountProperties(
                        stateEvent = stateEvent,
                        authToken = authToken
                    )
                }

                is UpdateAccountPropertiesEvent -> {
                    accountRepository.saveAccountProperties(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        email = stateEvent.email,
                        username = stateEvent.username
                    )
                }

                is ChangePasswordEvent -> {
                    accountRepository.updatePassword(
                        stateEvent = stateEvent,
                        authToken = authToken,
                        currentPassword = stateEvent.currentPassword,
                        newPassword = stateEvent.newPassword,
                        confirmNewPassword = stateEvent.confirmNewPassword
                    )
                }

                else -> {
                    flow {
                        emit(
                            DataState.error(
                                response = Response(
                                    message = INVALID_STATE_EVENT,
                                    uiComponentType = UIComponentType.None(),
                                    messageType = MessageType.Error()
                                ),
                                stateEvent = stateEvent
                            )
                        )
                    }
                }
            }
            launchJob(stateEvent, job)
        } ?: sessionManager.logout()
    }

    fun setAccountPropertiesData(accountProperties: AccountProperties) {
        val update = getCurrentViewStateOrNew()
        if (update.accountProperties == accountProperties) {
            return
        }
        update.accountProperties = accountProperties
        setViewState(update)
    }

    override fun initNewViewState(): AccountViewState {
        return AccountViewState()
    }

    fun logout() {
        sessionManager.logout()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}















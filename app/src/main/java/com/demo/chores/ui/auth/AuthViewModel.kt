package com.demo.chores.ui.auth

import com.demo.chores.models.AuthToken
import com.demo.chores.repository.auth.AuthRepository
import com.demo.chores.ui.BaseViewModel
import com.demo.chores.ui.auth.state.AuthStateEvent
import com.demo.chores.ui.auth.state.AuthViewState
import com.demo.chores.ui.auth.state.LoginFields
import com.demo.chores.ui.auth.state.RegistrationFields
import com.demo.chores.util.DataState
import com.demo.chores.util.ErrorHandling.Companion.INVALID_STATE_EVENT
import com.demo.chores.util.MessageType
import com.demo.chores.util.Response
import com.demo.chores.util.StateEvent
import com.demo.chores.util.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    val authRepository: AuthRepository
) : BaseViewModel<AuthViewState>() {
    override fun handleNewData(stateEvent: StateEvent?, data: AuthViewState) {
        data.authToken?.let { authToken ->
            setAuthToken(authToken)
        }
        _activeStateEventTracker.removeStateEvent(stateEvent)
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<AuthViewState>> = when (stateEvent) {
            is AuthStateEvent.LoginAttemptEvent -> {
                authRepository.attemptLogin(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    password = stateEvent.password
                )
            }

            is AuthStateEvent.RegisterAttemptEvent -> {
                authRepository.attemptRegistration(
                    stateEvent = stateEvent,
                    email = stateEvent.email,
                    username = stateEvent.username,
                    password = stateEvent.password,
                    confirmPassword = stateEvent.confirm_password
                )
            }

            is AuthStateEvent.CheckPreviousAuthEvent -> {
                authRepository.checkPreviousAuthUser(stateEvent)
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
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setRegistrationFields(registrationFields: RegistrationFields) {
        val update = getCurrentViewStateOrNew()
        if (update.registrationFields == registrationFields) {
            return
        }
        update.registrationFields = registrationFields
        setViewState(update)
    }

    fun setLoginFields(loginFields: LoginFields) {
        val update = getCurrentViewStateOrNew()
        if (update.loginFields == loginFields) {
            return
        }
        update.loginFields = loginFields
        setViewState(update)
    }

    fun setAuthToken(authToken: AuthToken) {
        val update = getCurrentViewStateOrNew()
        if (update.authToken == authToken) {
            return
        }
        update.authToken = authToken
        setViewState(update)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}

























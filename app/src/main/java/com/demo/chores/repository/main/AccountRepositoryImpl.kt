package com.demo.chores.repository.main

import android.util.Log
import com.demo.chores.api.GenericResponse
import com.demo.chores.api.main.OpenApiMainService
import com.demo.chores.di.main.MainScope
import com.demo.chores.models.AccountProperties
import com.demo.chores.models.AuthToken
import com.demo.chores.persistence.AccountPropertiesDao
import com.demo.chores.repository.NetworkBoundResource
import com.demo.chores.repository.safeApiCall
import com.demo.chores.session.SessionManager
import com.demo.chores.ui.main.account.state.AccountViewState
import com.demo.chores.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@FlowPreview
@MainScope
class AccountRepositoryImpl
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val accountPropertiesDao: AccountPropertiesDao,
    val sessionManager: SessionManager
) : AccountRepository {

    private val TAG: String = "AppDebug"

    override fun getAccountProperties(
        authToken: AuthToken,
        stateEvent: StateEvent
    ): Flow<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
                dispatcher = IO,
                stateEvent = stateEvent,
                apiCall = {
                    openApiMainService
                        .getAccountProperties("Token ${authToken.token!!}")
                },
                cacheCall = {
                    accountPropertiesDao.searchByPk(authToken.account_pk!!).value
                }

            ) {
            override suspend fun updateCache(networkObject: AccountProperties) {
                accountPropertiesDao.updateAccountProperties(
                    networkObject.pk,
                    networkObject.email,
                    networkObject.username
                )
            }

            override fun handleCacheSuccess(
                resultObj: AccountProperties
            ): DataState<AccountViewState> {
                return DataState.data(
                    response = null,
                    data = AccountViewState(
                        accountProperties = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }

        }.result
    }

    override fun saveAccountProperties(
        authToken: AuthToken,
        email: String,
        username: String,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO) {
            openApiMainService.saveAccountProperties(
                "Token ${authToken.token!!}",
                email,
                username
            )
        }
        emit(
            object : ApiResponseHandler<AccountViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(
                    resultObj: GenericResponse
                ): DataState<AccountViewState> {

                    val updatedAccountProperties = openApiMainService
                        .getAccountProperties("Token ${authToken.token!!}")

                    accountPropertiesDao.updateAccountProperties(
                        pk = updatedAccountProperties.pk,
                        email = updatedAccountProperties.email,
                        username = updatedAccountProperties.username
                    )

                    return DataState.data(
                        data = null,
                        response = Response(
                            message = resultObj.response,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        stateEvent = stateEvent
                    )
                }

            }.getResult()
        )
    }

    override fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String,
        stateEvent: StateEvent
    ) = flow {
        val apiResult = safeApiCall(IO) {
            openApiMainService.updatePassword(
                "Token ${authToken.token!!}",
                currentPassword,
                newPassword,
                confirmNewPassword
            )
        }
        emit(
            object : ApiResponseHandler<AccountViewState, GenericResponse>(
                response = apiResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(
                    resultObj: GenericResponse
                ): DataState<AccountViewState> {

                    return DataState.data(
                        data = null,
                        response = Response(
                            message = resultObj.response,
                            uiComponentType = UIComponentType.Toast(),
                            messageType = MessageType.Success()
                        ),
                        stateEvent = stateEvent
                    )
                }
            }.getResult()
        )
    }

}













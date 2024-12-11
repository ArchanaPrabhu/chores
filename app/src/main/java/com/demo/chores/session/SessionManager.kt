package com.demo.chores.session

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.chores.models.AuthToken
import com.demo.chores.persistence.AuthTokenDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
) {
    private val TAG: String = "AppDebug"

    private val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    fun login(newValue: AuthToken) {
        setValue(newValue)
    }

    fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Main) {
            if (_cachedToken.value != newValue) {
                _cachedToken.value = newValue?: AuthToken()
            }
        }
    }

    fun logout() {
        Log.d("Testing", "logout: ")

        CoroutineScope(IO).launch {
            var errorMessage: String? = null
            try {
                _cachedToken.value?.account_pk?.let {
                    authTokenDao.nullifyToken(it)
                } ?: throw CancellationException("Token Error. Logging out user")
            } catch (e: CancellationException) {
                Log.e(TAG, "logout: ${e.message}")
                errorMessage = e.message
            } catch (e: Exception) {
                Log.e(TAG, "logout : ${e.message}")
                errorMessage = errorMessage + "\n" + e.message
            } finally {
                errorMessage?.let {
                    Log.e(TAG, "logout ${errorMessage}")
                }
                Log.d(TAG, "logout: finally")
                setValue(null)
            }
        }
    }

}
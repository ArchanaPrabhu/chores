package com.demo.chores.ui.auth.state

import com.demo.chores.util.StateEvent

sealed class AuthStateEvent : StateEvent {

    data class LoginAttemptEvent(
        val email: String,
        val password: String
    ) : AuthStateEvent() {

        override fun errorInfo(): String {
            return "Login attempt failed."
        }
    }

    data class RegisterAttemptEvent(
        val email: String,
        val username: String,
        val password: String,
        val confirm_password: String
    ) : AuthStateEvent() {

        override fun errorInfo(): String {
            return "Register attempt failed."
        }
    }

    data class PopulateFromCacheEvent(
        val email: String,
        val password: String
    ) : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Error in populating dummy data for UI testing."
        }

    }

    class CheckPreviousAuthEvent() : AuthStateEvent() {

        override fun errorInfo(): String {
            return "Error checking for previously authenticated user."
        }
    }

    class None : AuthStateEvent() {

        override fun errorInfo(): String {
            return "None"
        }
    }
}

package com.demo.chores.ui

import com.demo.chores.util.Response
import com.demo.chores.util.StateMessageCallback


interface UICommunicationListener {

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun displayProgressBar(isLoading: Boolean)

    fun expandAppBar()

    fun hideSoftKeyboard()

    fun isStoragePermissionGranted(): Boolean
}
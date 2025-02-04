package com.demo.chores.ui

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.afollestad.materialdialogs.MaterialDialog
import com.demo.chores.R
import com.demo.chores.util.StateMessageCallback

private val TAG: String = "AppDebug"

fun Activity.displayToast(
    @StringRes message:Int,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this, message,Toast.LENGTH_LONG).show()
    stateMessageCallback.removeMessageFromStack()
}

fun Activity.displayToast(
    message:String,
    stateMessageCallback: StateMessageCallback
){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    stateMessageCallback.removeMessageFromStack()
}

fun Activity.displaySuccessDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback
){
    MaterialDialog(this)
        .show{
            title(R.string.text_success)
            message(text = message)
            positiveButton(R.string.text_ok)
            setOnDismissListener {
                stateMessageCallback.removeMessageFromStack()
            }
        }
}

fun Activity.displayErrorDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback
){
    MaterialDialog(this)
        .show{
            title(R.string.text_error)
            message(text = message)
            positiveButton(R.string.text_ok)
            setOnDismissListener {
                Log.d(TAG, "dismissing dialog: ")
                stateMessageCallback.removeMessageFromStack()
            }
        }
}

fun Activity.displayInfoDialog(
    message: String?,
    stateMessageCallback: StateMessageCallback
){
    MaterialDialog(this)
        .show{
            title(R.string.text_info)
            message(text = message)
            positiveButton(R.string.text_ok)
            setOnDismissListener {
                stateMessageCallback.removeMessageFromStack()
            }
        }
}

fun Activity.areYouSureDialog(
    message: String,
    callback: AreYouSureCallback,
    stateMessageCallback: StateMessageCallback
){
    MaterialDialog(this)
        .show{
            title(R.string.are_you_sure)
            message(text = message)
            negativeButton(R.string.text_cancel){
                callback.cancel()
            }
            positiveButton(R.string.text_yes){
                callback.proceed()
            }
            setOnDismissListener {
                stateMessageCallback.removeMessageFromStack()
            }
        }
}


interface AreYouSureCallback {

    fun proceed()

    fun cancel()
}









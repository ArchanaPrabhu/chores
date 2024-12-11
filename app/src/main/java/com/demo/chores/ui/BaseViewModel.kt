package com.demo.chores.ui

import android.util.Log
import androidx.constraintlayout.motion.utils.ViewState
import androidx.lifecycle.*
import com.demo.chores.ui.main.account.state.AccountStateEvent
import com.demo.chores.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@FlowPreview
@ExperimentalCoroutinesApi
abstract class BaseViewModel<ViewState> : ViewModel()
{
    val TAG: String = "AppDebug"

    protected var dataChannel: MutableStateFlow<DataState<ViewState>?> = MutableStateFlow(null)

    protected val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    protected val _activeStateEventTracker: ActiveStateEventTracker = ActiveStateEventTracker()
    private val messageStack = MessageStack()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val numActiveJobs: LiveData<Int> = _activeStateEventTracker.numActiveJobs

    val stateMessage: LiveData<StateMessage?>
        get() = messageStack.stateMessage

    fun getMessageStackSize(): Int{
        return messageStack.size
    }

    fun setupChannel(){
        cancelActiveJobs()
        _activeStateEventTracker.setupNewChannelScope(CoroutineScope(Main))

         dataChannel
            .onEach{ dataState ->
                dataState?.data?.let { data ->
                    handleNewData(dataState.stateEvent, data)
                }
                dataState?.stateMessage?.let { stateMessage ->
                    handleNewStateMessage(dataState?.stateEvent, stateMessage)
                }
            }
            .launchIn(_activeStateEventTracker.getChannelScope())
    }

    abstract fun handleNewData(stateEvent: StateEvent?, data: ViewState)

    abstract fun setStateEvent(stateEvent: StateEvent)

    fun handleNewStateMessage(stateEvent: StateEvent?, stateMessage: StateMessage){
        appendStateMessage(stateMessage)
        _activeStateEventTracker.removeStateEvent(stateEvent)
    }

    fun launchJob(
        stateEvent: StateEvent,
        jobFunction: Flow<DataState<ViewState>>
    ){
        if(!isJobAlreadyActive(stateEvent)){
            Log.d(TAG, "launching job: ")
            _activeStateEventTracker.addStateEvent(stateEvent)
            jobFunction
                .onEach { dataState ->
                    offerToDataChannel(dataState)
                }
                .launchIn(_activeStateEventTracker.getChannelScope())
        }
    }

    fun areAnyJobsActive(): Boolean{
        return _activeStateEventTracker.numActiveJobs.value?.let {
            it > 0
        }?: false
    }

    fun getNumActiveJobs(): Int {
        return _activeStateEventTracker.numActiveJobs.value ?: 0
    }

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return _activeStateEventTracker.isStateEventActive(stateEvent)
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun offerToDataChannel(dataState: DataState<ViewState>){
        dataChannel.value = dataState
    }

    fun getCurrentViewStateOrNew(): ViewState {
        val value = viewState.value?.let{
            it
        }?: initNewViewState()
        return value
    }

    fun setViewState(viewState: ViewState){
        _viewState.value = viewState
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun clearStateMessage(index: Int = 0){
        messageStack.removeAt(index)
    }

    open fun cancelActiveJobs(){
        if(areAnyJobsActive()){
            Log.d(TAG, "cancel active jobs: ${getNumActiveJobs()}")
            _activeStateEventTracker.cancelJobs()
        }
    }

    abstract fun initNewViewState(): ViewState

}

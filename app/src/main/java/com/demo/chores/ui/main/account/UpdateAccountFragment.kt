package com.demo.chores.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.demo.chores.R
import com.demo.chores.databinding.FragmentUpdateAccountBinding
import com.demo.chores.models.AccountProperties
import com.demo.chores.ui.main.account.state.ACCOUNT_VIEW_STATE_BUNDLE_KEY
import com.demo.chores.ui.main.account.state.AccountStateEvent
import com.demo.chores.ui.main.account.state.AccountViewState
import com.demo.chores.util.StateMessageCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class UpdateAccountFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : BaseAccountFragment(R.layout.fragment_update_account) {

    var _binding: FragmentUpdateAccountBinding? = null
    val binding: FragmentUpdateAccountBinding
        get() = _binding!!
    val viewModel: AccountViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Restore state after process death
        savedInstanceState?.let { inState ->
            (inState[ACCOUNT_VIEW_STATE_BUNDLE_KEY] as AccountViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObservers()
    }

    override fun setupChannel() {
        Log.d(TAG, "setupChannel ")
        viewModel.setupChannel()
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.accountProperties?.let {
                    Log.d(TAG, "UpdateAccountFragment, ViewState: ${it}")
                    setAccountDataFields(it)
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->

            Log.d(TAG, "stack size: ${viewModel.getMessageStackSize()}")
            Log.d(TAG, "state message: ${stateMessage}")
            stateMessage?.let {
                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    private fun setAccountDataFields(accountProperties: AccountProperties) {
        if (binding.inputEmail.text.isNullOrBlank()) {
            binding.inputEmail.setText(accountProperties.email)
        }
        if (binding.inputUsername.text.isNullOrBlank()) {
            binding.inputUsername.setText(accountProperties.username)
        }
    }

    private fun saveChanges() {
        viewModel.setStateEvent(
            AccountStateEvent.UpdateAccountPropertiesEvent(
                binding.inputEmail.text.toString(),
                binding.inputUsername.text.toString()
            )
        )
        uiCommunicationListener.hideSoftKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                saveChanges()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}














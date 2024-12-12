package com.demo.chores.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.demo.chores.R
import com.demo.chores.databinding.FragmentAccountBinding
import com.demo.chores.models.AccountProperties
import com.demo.chores.ui.main.account.state.ACCOUNT_VIEW_STATE_BUNDLE_KEY
import com.demo.chores.ui.main.account.state.AccountStateEvent
import com.demo.chores.ui.main.account.state.AccountStateEvent.*
import com.demo.chores.ui.main.account.state.AccountViewState
import com.demo.chores.util.StateMessageCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AccountFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): BaseAccountFragment(R.layout.fragment_account) {

    var _binding : FragmentAccountBinding? = null

    val binding : FragmentAccountBinding
        get() = _binding!!

    val viewModel: AccountViewModel by viewModels{
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
         _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.changePassword.setOnClickListener{
            findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        subscribeObservers()
    }

    override fun setupChannel() {
        viewModel.setupChannel()
    }

    private fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState->
            Log.d(TAG, "AccountFragment, ViewState: ${viewState}")
            if(viewState != null){
                viewState.accountProperties?.let{
                    setAccountDataFields(it)
                }
            }
        })

        viewModel.numActiveJobs.observe(viewLifecycleOwner, Observer { jobCounter ->
            uiCommunicationListener.displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->


            stateMessage?.let {
                uiCommunicationListener.onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object: StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()
//        viewModel.setStateEvent(GetAccountPropertiesEvent())
        viewModel.setStateEvent(DemoAccountPropertiesEvent())
    }

    private fun setAccountDataFields(accountProperties: AccountProperties){
        binding.email?.setText(accountProperties.email)
        binding.username?.setText(accountProperties.username)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_view_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit -> {
                findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
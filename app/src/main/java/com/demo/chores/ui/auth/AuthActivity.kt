package com.demo.chores.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.demo.chores.BaseApplication
import com.demo.chores.R
import com.demo.chores.databinding.ActivityAuthBinding
import com.demo.chores.fragments.auth.AuthNavHostFragment
import com.demo.chores.repository.auth.AuthRepositoryImpl
import com.demo.chores.ui.BaseActivity
import com.demo.chores.ui.auth.AuthViewModel
import com.demo.chores.ui.auth.state.AuthStateEvent
import com.demo.chores.ui.main.MainActivity
import com.demo.chores.util.StateMessageCallback
import com.demo.chores.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    val viewModel: AuthViewModel by viewModels {
        providerFactory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        subscribeObservers()
        onRestoreInstanceState()
        viewModel.setupChannel()
    }

    fun onRestoreInstanceState() {
        val host = supportFragmentManager.findFragmentById(R.id.auth_fragments_container)
        host?.let {
            // do nothing
        } ?: createNavHost()
    }

    private fun createNavHost() {
        val navHost = AuthNavHostFragment.create(
            R.navigation.auth_nav_graph
        )
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.auth_fragments_container,
                navHost,
                getString(R.string.AuthNavHost)
            )
            .setPrimaryNavigationFragment(navHost)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        checkPreviousAuthUser()
    }

    private fun subscribeObservers() {

        viewModel.viewState.observe(this, Observer { viewState ->
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthViewState: ${viewState}")
            viewState.authToken?.let {
                sessionManager.login(it)
            }
        })

        viewModel.numActiveJobs.observe(this, Observer { jobCounter ->
            Log.d(TAG, "active jobs: ${jobCounter}")
            displayProgressBar(viewModel.areAnyJobsActive())
        })

        viewModel.stateMessage.observe(this, Observer { stateMessage ->

            stateMessage?.let {
                if (it.response.message.equals(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE)) {
                    onFinishCheckPreviousAuthUser()
                }
                onResponseReceived(
                    response = it.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })

        sessionManager.cachedToken.observe(this, Observer { dataState ->
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthDataState: ${dataState}")
            dataState.let { authToken ->
                if (authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                    navMainActivity()
                }
            }
            CoroutineScope(IO).launch {
                Log.d("Testing", (viewModel.authRepository as AuthRepositoryImpl).authTokenDao.searchByPk(1).toString())
                Log.d("Testing", (viewModel.authRepository as AuthRepositoryImpl).accountPropertiesDao.searchByPk(1).toString())
            }
        })
    }

    fun navMainActivity() {
        Log.d(TAG, "navMainActivity: called.")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        (application as BaseApplication).releaseAuthComponent()
    }

    private fun checkPreviousAuthUser() {
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAuthEvent())
    }

    private fun onFinishCheckPreviousAuthUser() {
        binding.fragmentContainer.visibility = View.VISIBLE
    }

    override fun inject() {
        (application as BaseApplication).authComponent()
            .inject(this)
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun expandAppBar() {
        // ignore
    }


}

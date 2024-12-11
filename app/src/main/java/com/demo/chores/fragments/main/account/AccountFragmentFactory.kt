package com.demo.chores.fragments.main.account

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.demo.chores.di.main.MainScope
import com.demo.chores.ui.main.account.AccountFragment
import com.demo.chores.ui.main.account.ChangePasswordFragment
import com.demo.chores.ui.main.account.UpdateAccountFragment
import javax.inject.Inject

@MainScope
class AccountFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            AccountFragment::class.java.name -> {
                AccountFragment(viewModelFactory)
            }

            ChangePasswordFragment::class.java.name -> {
                ChangePasswordFragment(viewModelFactory)
            }

            UpdateAccountFragment::class.java.name -> {
                UpdateAccountFragment(viewModelFactory)
            }

            else -> {
                AccountFragment(viewModelFactory)
            }
        }


}
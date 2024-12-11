package com.demo.chores.di.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.chores.di.main.keys.MainViewModelKey
import com.demo.chores.ui.main.account.AccountViewModel
import com.demo.chores.ui.main.blog.viewmodel.BlogViewModel
import com.demo.chores.ui.main.create_blog.CreateBlogViewModel
import com.demo.chores.viewmodels.MainViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: MainViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @MainViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accoutViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(BlogViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: BlogViewModel): ViewModel

    @Binds
    @IntoMap
    @MainViewModelKey(CreateBlogViewModel::class)
    abstract fun bindCreateBlogViewModel(createBlogViewModel: CreateBlogViewModel): ViewModel
}









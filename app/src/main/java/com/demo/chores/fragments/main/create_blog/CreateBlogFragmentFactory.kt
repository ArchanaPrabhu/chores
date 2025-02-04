package com.demo.chores.fragments.main.create_blog

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.demo.chores.di.main.MainScope
import com.demo.chores.ui.main.account.AccountFragment
import com.demo.chores.ui.main.blog.BlogFragment
import com.demo.chores.ui.main.blog.UpdateBlogFragment
import com.demo.chores.ui.main.blog.ViewBlogFragment
import com.demo.chores.ui.main.create_blog.CreateBlogFragment
import javax.inject.Inject

@MainScope
class CreateBlogFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val requestManager: RequestManager
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            CreateBlogFragment::class.java.name -> {
                CreateBlogFragment(viewModelFactory, requestManager)
            }

            else -> {
                CreateBlogFragment(viewModelFactory, requestManager)
            }
        }


}
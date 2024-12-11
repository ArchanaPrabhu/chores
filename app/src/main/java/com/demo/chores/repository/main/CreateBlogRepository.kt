package com.demo.chores.repository.main

import com.demo.chores.di.main.MainScope
import com.demo.chores.models.AuthToken
import com.demo.chores.ui.main.create_blog.state.CreateBlogViewState
import com.demo.chores.util.DataState
import com.demo.chores.util.StateEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

@FlowPreview
@MainScope
interface CreateBlogRepository {

    fun createNewBlogPost(
        authToken: AuthToken,
        title: RequestBody,
        body: RequestBody,
        image: MultipartBody.Part?,
        stateEvent: StateEvent
    ): Flow<DataState<CreateBlogViewState>>
}
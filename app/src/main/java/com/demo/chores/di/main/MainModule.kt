package com.demo.chores.di.main

import android.content.SharedPreferences
import com.demo.chores.api.main.OpenApiMainService
import com.demo.chores.persistence.AccountPropertiesDao
import com.demo.chores.persistence.AppDatabase
import com.demo.chores.persistence.BlogPostDao
import com.demo.chores.repository.main.AccountRepositoryImpl
import com.demo.chores.repository.main.BlogRepositoryImpl
import com.demo.chores.repository.main.CreateBlogRepositoryImpl
import com.demo.chores.session.SessionManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.FlowPreview
import retrofit2.Retrofit

@FlowPreview
@Module
object MainModule {

    @JvmStatic
    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideAccountRepository(
        openApiMainService: OpenApiMainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager,
        sharedPreferences: SharedPreferences
    ): AccountRepositoryImpl {
        return AccountRepositoryImpl(
            openApiMainService,
            accountPropertiesDao,
            sessionManager,
            sharedPreferences
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideBlogPostDao(db: AppDatabase): BlogPostDao {
        return db.getBlogPostDao()
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideBlogRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): BlogRepositoryImpl {
        return BlogRepositoryImpl(
            openApiMainService,
            blogPostDao,
            sessionManager
        )
    }

    @JvmStatic
    @MainScope
    @Provides
    fun provideCreateBlogRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): CreateBlogRepositoryImpl {
        return CreateBlogRepositoryImpl(
            openApiMainService, blogPostDao,
            sessionManager
        )
    }
}


















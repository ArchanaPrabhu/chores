package com.demo.chores.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demo.chores.models.AccountProperties
import com.demo.chores.models.AuthToken
import com.demo.chores.models.BlogPost

@Database(entities = [AuthToken::class, AccountProperties::class, BlogPost::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    abstract fun getBlogPostDao(): BlogPostDao

    companion object {
        val DATABASE_NAME: String = "app_db"
    }
}











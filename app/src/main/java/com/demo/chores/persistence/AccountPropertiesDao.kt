package com.demo.chores.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.chores.models.AccountProperties

@Dao
interface AccountPropertiesDao {

    @Query("SELECT * FROM account_properties WHERE email = :email")
    fun searchByEmail(email: String): AccountProperties?

    @Query("SELECT * FROM account_properties WHERE pk = :pk")
    fun searchByPk(pk: Int): LiveData<AccountProperties>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndReplace(accountProperties: AccountProperties): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(accountProperties: AccountProperties): Long

    @Query("UPDATE account_properties SET email = :email, username = :username WHERE pk = :pk")
    fun updateAccountProperties(pk: Int, email: String, username: String)

}


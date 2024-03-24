package com.sugarygary.gitconnect.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sugarygary.gitconnect.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM favorite_users")
    suspend fun getAll(): List<FavoriteUser>

    @Query("SELECT EXISTS(SELECT * FROM favorite_users WHERE login = :login )")
    suspend fun checkFavorite(login: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(user: FavoriteUser)

    @Query("DELETE FROM favorite_users WHERE login = :login")
    suspend fun removeFavorite(login: String)

}
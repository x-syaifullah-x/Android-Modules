package id.xxx.module.auth.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import id.xxx.module.auth.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao(val room: RoomDatabase) {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(e: UserEntity): Long

    @Query("UPDATE ${UserEntity.TABLE_NAME} SET ${UserEntity.COLUMN_NAME_IS_LOGGED_IN}=0")
    abstract suspend fun isLoggedInClear(): Int

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.COLUMN_NAME_IS_LOGGED_IN}=1")
    abstract fun getUserLoggedIn(): Flow<UserEntity?>
}
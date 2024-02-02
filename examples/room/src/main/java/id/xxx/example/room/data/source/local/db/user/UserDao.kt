package id.xxx.example.room.data.source.local.db.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import id.xxx.example.room.data.source.local.model.Result
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao(val room: RoomDatabase) {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    /**
     * Successfully insert @return [UserEntity.pk]
     * Failed to insert data @return -1
     */
    abstract suspend fun insert(userEntity: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    /**
     *  Successfully insert @return list of [UserEntity.pk]
     *  Failed to insert data @return -1
     */
    abstract suspend fun insert(entities: List<UserEntity>): List<Long>

    @Update
    /**
     * Successfully update will @return 1
     * Nothing update will @return 0
     *
     */
    abstract suspend fun update(entity: UserEntity): Int

    @Update
    /**
     * Will @return the amount of data that was successfully updated
     * Nothing update will @return 0
     *
     */
    abstract suspend fun update(entities: List<UserEntity>): Int

    @Query("UPDATE ${UserEntity.TABLE_NAME} set ${UserEntity.COLUMN_NAME}=:name where ${UserEntity.COLUMN_PK}=:pk")
    /**
     * @param pk is [UserEntity.pk]
     * @param name is [UserEntity.name]
     * Successfully update will @return 1
     * Nothing update will @return 0
     *
     */
    abstract suspend fun updateName(pk: Long, name: String): Int

    @Delete
    /**
     * Successfully delete will @return 1
     * Nothing delete will @return 0
     */
    abstract suspend fun delete(entity: UserEntity): Int

    @Query("DELETE FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.COLUMN_PK}=:pk")
    /**
     * Successfully delete will @return 1
     * nothing delete will @return 0
     * @param pk is [UserEntity.pk]
     */
    abstract suspend fun delete(pk: Long): Int

    @Query("DELETE FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.COLUMN_PK} IN (:pk)")
    /**
     * Will @return the amount of data that was successfully delete
     * nothing delete will @return 0
     * @param pk is [UserEntity.pk]
     */
    abstract suspend fun delete(pk: List<Long>): Int


    @Transaction
    @Query("SELECT * FROM ${UserEntity.TABLE_NAME}")
    abstract suspend fun query(): List<Result>

    @Query("SELECT * FROM ${UserEntity.TABLE_NAME}")
    abstract fun queryAsFlow(): Flow<List<UserEntity>>

    suspend fun deleteTableAndResetPrimaryKey() {
        deleteTable()
        resetPrimaryKey()
    }

    @Query("DELETE FROM ${UserEntity.TABLE_NAME}")
    /**
     * Successfully deleteTable will @return 1
     * nothing delete will @return 0
     */
    abstract suspend fun deleteTable(): Int

    @Query("DELETE FROM sqlite_sequence WHERE name='${UserEntity.TABLE_NAME}'")
    /**
     * Successfully resetPrimaryKey will @return 1
     * nothing delete will @return 0
     */
    abstract suspend fun resetPrimaryKey(): Int
}
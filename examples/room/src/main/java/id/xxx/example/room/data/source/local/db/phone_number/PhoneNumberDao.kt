package id.xxx.example.room.data.source.local.db.phone_number

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class PhoneNumberDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    /**
     * Successfully insert @return [PhoneNumberEntity.pk]
     * Failed to insert data @return -1
     */
    abstract suspend fun insert(entity: PhoneNumberEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    /**
     * Successfully insert @return list of [PhoneNumberEntity.pk]
     * Failed to insert data @return list of -1
     */
    abstract suspend fun insert(entities: List<PhoneNumberEntity>): List<Long>
}
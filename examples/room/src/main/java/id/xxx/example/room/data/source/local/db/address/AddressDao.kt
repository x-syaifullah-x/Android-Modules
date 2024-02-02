package id.xxx.example.room.data.source.local.db.address

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class AddressDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    /**
     * Successfully insert @return [AddressEntity.pk]
     * Failed to insert data @return -1
     */
    abstract suspend fun insert(entity: AddressEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    /**
     * Successfully insert @return list of [AddressEntity.pk]
     * Failed to insert data @return -1
     */
    abstract suspend fun insert(entities: List<AddressEntity>): List<Long>
}
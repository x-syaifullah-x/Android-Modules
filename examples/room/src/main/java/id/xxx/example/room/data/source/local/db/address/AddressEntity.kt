package id.xxx.example.room.data.source.local.db.address

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import id.xxx.example.room.data.source.local.db.user.UserEntity
import java.lang.Error

@Entity(
    tableName = AddressEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = [UserEntity.COLUMN_PK],
            childColumns = [AddressEntity.COLUMN_USER_PK],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class AddressEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_PK)
    val pk: Long? = null,

    @ColumnInfo(name = COLUMN_USER_PK, index = true)
    val userPk: Long,
) {

    companion object {
        const val TABLE_NAME = "address"
        const val COLUMN_PK = "pk"
        const val COLUMN_USER_PK = "user_pk"
    }
}
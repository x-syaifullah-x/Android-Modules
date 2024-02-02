package id.xxx.example.room.data.source.local.db.phone_number

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import id.xxx.example.room.data.source.local.db.user.UserEntity

@Entity(
    tableName = PhoneNumberEntity.TABLE_NAME,
    indices = [
        Index(
            value = [PhoneNumberEntity.COLUMN_NUMBER],
            unique = true
        ),
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = [UserEntity.COLUMN_PK],
            childColumns = [PhoneNumberEntity.COLUMN_USER_PK],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class PhoneNumberEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_PK)
    private val _pk: Long? = null,

    @ColumnInfo(name = COLUMN_USER_PK, index = true)
    val userPk: Long,

    @ColumnInfo(name = COLUMN_NUMBER)
    val number: String,
) {
    val pk get() = _pk

    companion object {
        const val TABLE_NAME = "phone_number"
        const val COLUMN_PK = "pk"
        const val COLUMN_USER_PK = "user_pk"
        const val COLUMN_NUMBER = "number"
    }
}
package id.xxx.module.auth.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserEntity.TABLE_NAME)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_NAME_UID)
    val uid: String,

    @ColumnInfo(name = COLUMN_NAME_IS_LOGGED_IN)
    val isLoggedIn: Boolean = false
) {

    companion object {

        const val TABLE_NAME = "users"
        const val COLUMN_NAME_UID = "uid"
        const val COLUMN_NAME_IS_LOGGED_IN = "is_logged_in"
    }
}
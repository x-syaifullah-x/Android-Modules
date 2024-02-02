package id.xxx.example.room.data.source.local.model

import androidx.room.Embedded
import androidx.room.Relation
import id.xxx.example.room.data.source.local.db.address.AddressEntity
import id.xxx.example.room.data.source.local.db.phone_number.PhoneNumberEntity
import id.xxx.example.room.data.source.local.db.user.UserEntity

data class Result(
    @Embedded
    val user: UserEntity,

    @Relation(
        parentColumn = UserEntity.COLUMN_PK,
        entityColumn = AddressEntity.COLUMN_USER_PK,
    )
    val addresses: List<AddressEntity>,

    @Relation(
        parentColumn = UserEntity.COLUMN_PK,
        entityColumn = PhoneNumberEntity.COLUMN_USER_PK,
    )
    val phoneNumber: List<PhoneNumberEntity>
)
package id.xxx.example.room.data.source.local

import android.content.Context
import androidx.room.withTransaction
import id.xxx.example.room.data.model.Data
import id.xxx.example.room.data.source.local.db.AppDatabase
import id.xxx.example.room.data.source.local.db.address.AddressEntity
import id.xxx.example.room.data.source.local.db.user.UserEntity
import kotlin.random.Random

class LocalSourceData(private val db: AppDatabase) {

    companion object {

        @Volatile
        private var INSTANCE: LocalSourceData? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocalSourceData(AppDatabase.getInstance(context))
                    .also { INSTANCE = it }
            }
    }

    val a = Random(Int.MAX_VALUE)

    suspend fun save(data: Data) {
        val userDao = db.userDao()
        userDao.room.withTransaction {
            val userEntity = UserEntity(name = data.name)
            val userPK = userDao.insert(userEntity)
            val addressDao = db.addressDao()
            val phoneNumberDao = db.phoneNumberDao()
            if (userPK > 0) {
                try {
                    val dataAddress = data.address
                    if (dataAddress.isNotEmpty()) {
                        val address = dataAddress.map {
                            AddressEntity(userPk = userPK)
                        }
                        addressDao.insert(address)
                    }

//                    val phoneNumber = listOf(
//                        PhoneNumberEntity(userPk = userPK, number = a.nextInt().toString()),
//                        PhoneNumberEntity(userPk = userPK, number = a.nextInt().toString()),
//                        PhoneNumberEntity(userPk = userPK, number = a.nextInt().toString()),
//                    )
//                    println(phoneNumberDao.insert(phoneNumber))
                } catch (e: Throwable) {
                    println("error: $${e.message}")
                }
            }
        }

        val results = userDao.query()
        println("results.size: ${results.size}")
        results.forEach { r ->
            println("r.user: ${r.user}")
            println("r.addresses.size: ${r.addresses.size}")
            println("r.phoneNumber.size: ${r.phoneNumber.size}")
        }
    }
}
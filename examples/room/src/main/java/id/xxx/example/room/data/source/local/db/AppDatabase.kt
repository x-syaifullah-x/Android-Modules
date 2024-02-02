package id.xxx.example.room.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.xxx.example.room.data.source.local.db.address.AddressDao
import id.xxx.example.room.data.source.local.db.address.AddressEntity
import id.xxx.example.room.data.source.local.db.phone_number.PhoneNumberDao
import id.xxx.example.room.data.source.local.db.phone_number.PhoneNumberEntity
import id.xxx.example.room.data.source.local.db.user.UserDao
import id.xxx.example.room.data.source.local.db.user.UserEntity

@Database(
    entities = [
        UserEntity::class,
        AddressEntity::class,
        PhoneNumberEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(AppDatabase::class.java) {
//                instance ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "app_database"
//                )
                instance ?: Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
    }

    abstract fun userDao(): UserDao

    abstract fun addressDao(): AddressDao

    abstract fun phoneNumberDao(): PhoneNumberDao

}
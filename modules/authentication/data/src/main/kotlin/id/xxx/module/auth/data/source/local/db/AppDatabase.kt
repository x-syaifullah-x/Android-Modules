package id.xxx.module.auth.data.source.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.xxx.module.auth.data.source.local.dao.UserDao
import id.xxx.module.auth.data.source.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context?) =
            instance ?: synchronized(AppDatabase::class.java) {
                val appContext = context?.applicationContext
                    ?: throw NullPointerException("Context")
//                instance ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "app_database"
//                )
                instance ?: Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
    }

    abstract val userDao: UserDao
}
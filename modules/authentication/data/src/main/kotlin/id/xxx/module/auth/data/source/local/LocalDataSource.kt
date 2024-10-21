package id.xxx.module.auth.data.source.local

import androidx.room.withTransaction
import id.xxx.module.auth.data.source.local.dao.UserDao
import id.xxx.module.auth.data.source.local.entity.UserEntity

class LocalDataSource private constructor(private val dao: UserDao) {

    companion object {

        @Volatile
        private var _sInstance: LocalDataSource? = null

        fun getInstance(dao: UserDao) = _sInstance ?: synchronized(this) {
            _sInstance ?: LocalDataSource(dao = dao).also { _sInstance = it }
        }
    }

    fun getUserLoggedIn() = dao.getUserLoggedIn()

    suspend fun save(e: UserEntity): Boolean {
        if (e.isLoggedIn) {
            return dao.room.withTransaction {
                dao.isLoggedInClear()
                dao.insert(e) > 0
            }
        }
        return dao.insert(e) > 0
    }
}
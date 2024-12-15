package id.xxx.module.auth.domain.repository

import id.xxx.module.auth.domain.model.AuthenticationType
import id.xxx.module.auth.domain.model.UserModel
import id.xxx.module.common.Resources
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun sign(type: AuthenticationType): Flow<Resources<UserModel>>

    fun getCurrentUser(): Flow<Resources<UserModel?>>
}
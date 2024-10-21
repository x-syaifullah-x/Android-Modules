package id.xxx.module.auth.domain.repository

import id.xxx.module.auth.domain.model.SignModel
import id.xxx.module.auth.domain.model.TypeSignIn
import id.xxx.module.auth.domain.model.TypeSignUp
import id.xxx.module.auth.domain.model.UserModel
import id.xxx.module.common.Resources
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signUp(type: TypeSignUp): Flow<Resources<SignModel>>

    fun signIn(type: TypeSignIn): Flow<Resources<SignModel>>

    fun getCurrentUser(): Flow<Resources<UserModel?>>
}
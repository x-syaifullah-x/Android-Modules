package id.xxx.module.auth.repository

import id.xxx.module.auth.model.Code
import id.xxx.module.auth.model.PasswordResetModel
import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.UpdateType
import id.xxx.module.auth.model.User
import id.xxx.module.auth.model.PhoneVerificationModel
import id.xxx.module.common.Resources
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signIn(type: SignInType): Flow<Resources<User>>

    fun signUp(type: SignUpType): Flow<Resources<User>>

    fun sendCode(code: Code.PhoneVerification): Flow<Resources<PhoneVerificationModel>>

    fun sendCode(code: Code.PasswordReset): Flow<Resources<PasswordResetModel>>

    fun update(type: UpdateType): Flow<Resources<String>>
}
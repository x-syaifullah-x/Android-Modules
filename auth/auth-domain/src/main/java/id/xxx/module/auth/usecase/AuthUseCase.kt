package id.xxx.module.auth.usecase

import id.xxx.module.auth.model.OobType
import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.UpdateType
import id.xxx.module.auth.model.User
import id.xxx.module.auth.model.VerificationCodeResult
import id.xxx.module.common.Resources
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {

    fun currentUser(): Flow<Resources<User>>

    fun signIn(type: SignInType): Flow<Resources<User>>

    fun signUp(type: SignUpType): Flow<Resources<User>>

    fun sendVerificationCode(
        phoneNumber: String, recaptchaResponse: String
    ): Flow<Resources<VerificationCodeResult>>

    fun signOut(): Flow<Resources<Boolean>>

    fun sendOobCode(type: OobType): Flow<Resources<String>>

    fun resetPassword(oobCode: String, newPassword: String): Flow<Resources<String>>

    fun update(type: UpdateType): Flow<Resources<String>>
}
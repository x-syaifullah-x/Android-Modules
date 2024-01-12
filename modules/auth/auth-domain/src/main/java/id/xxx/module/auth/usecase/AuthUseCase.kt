package id.xxx.module.auth.usecase

import id.xxx.module.auth.model.LookupModel
import id.xxx.module.auth.model.PasswordResetModel
import id.xxx.module.auth.model.PhoneVerificationModel
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.model.VerifyEmailModel
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.model.parms.UpdateType
import id.xxx.module.common.Resources
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {

    fun sign(type: SignType): Flow<Resources<SignModel>>

    fun sendCode(code: Code.PhoneVerification): Flow<Resources<PhoneVerificationModel>>

    fun sendCode(code: Code.PasswordReset): Flow<Resources<PasswordResetModel>>

    fun sendCode(code: Code.VerifyEmail): Flow<Resources<VerifyEmailModel>>

    fun lookup(idToken: String): Flow<Resources<LookupModel>>

    fun update(type: UpdateType): Flow<Resources<String>>
}
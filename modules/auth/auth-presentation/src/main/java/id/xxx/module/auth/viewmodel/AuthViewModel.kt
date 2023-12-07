package id.xxx.module.auth.viewmodel

import androidx.lifecycle.ViewModel
import id.xxx.module.auth.model.PhoneVerificationModel
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignInType
import id.xxx.module.auth.model.parms.SignUpType
import id.xxx.module.auth.usecase.AuthUseCase
import id.xxx.module.common.Resources
import kotlinx.coroutines.flow.map

class AuthViewModel(
    private val useCase: AuthUseCase
) : ViewModel() {

    fun signUp(type: SignUpType) =
        useCase.signUp(type)

    fun signIn(type: SignInType) =
        useCase.signIn(type)

    fun sendCode(code: Code.PhoneVerification) =
        useCase.sendCode(code)

    fun sendCode(code: Code.PasswordReset) =
        useCase.sendCode(code)

    fun sendCode(code: Code.VerifyEmail) =
        useCase.sendCode(code)

    fun lookup(idToken: String) =
        useCase.lookup(idToken)
}
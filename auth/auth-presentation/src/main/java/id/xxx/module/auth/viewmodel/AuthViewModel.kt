package id.xxx.module.auth.viewmodel

import androidx.lifecycle.ViewModel
import id.xxx.module.auth.model.Code
import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.usecase.AuthUseCase

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
}
package id.xxx.module.auth.viewmodel

import androidx.lifecycle.ViewModel
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.usecase.AuthUseCase

class AuthViewModel(
    private val useCase: AuthUseCase
) : ViewModel() {

    fun sign(type: SignType) =
        useCase.sign(type)

    fun sendCode(code: Code.PhoneVerification) =
        useCase.sendCode(code)

    fun sendCode(code: Code.PasswordReset) =
        useCase.sendCode(code)

    fun sendCode(code: Code.VerifyEmail) =
        useCase.sendCode(code)

    fun lookup(idToken: String) =
        useCase.lookup(idToken)
}
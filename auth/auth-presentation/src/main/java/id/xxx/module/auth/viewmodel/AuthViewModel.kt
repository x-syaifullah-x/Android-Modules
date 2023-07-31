package id.xxx.module.auth.viewmodel

import androidx.lifecycle.ViewModel
import id.xxx.module.auth.model.OobType
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

    fun sendVerificationCode(phoneNumber: String, recaptchaToken: String) =
        useCase.sendVerificationCode(phoneNumber, recaptchaToken)

    fun sendOobCode(email: String) = useCase.sendOobCode(
        OobType.PasswordReset(
            email = email
        )
    )
}
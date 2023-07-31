package id.xxx.module.auth.usecase

import id.xxx.module.auth.model.Code
import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.UpdateType
import id.xxx.module.auth.repository.AuthRepository

class AuthUseCaseImpl private constructor(
    private val repo: AuthRepository
) : AuthUseCase {

    companion object {
        @Volatile
        private var INSTANCE: AuthUseCase? = null

        fun getInstance(repo: AuthRepository) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthUseCaseImpl(repo)
                    .also { INSTANCE = it }
            }
    }

    override fun signIn(type: SignInType) =
        repo.signIn(type)

    override fun signUp(type: SignUpType) =
        repo.signUp(type)

    override fun sendCode(code: Code.PhoneVerification) =
        repo.sendCode(code)

    override fun sendCode(code: Code.PasswordReset) =
        repo.sendCode(code)

    override fun update(type: UpdateType) =
        repo.update(type)
}
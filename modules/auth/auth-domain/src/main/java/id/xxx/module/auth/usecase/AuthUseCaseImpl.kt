package id.xxx.module.auth.usecase

import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.model.parms.UpdateType
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

    override fun sign(type: SignType) =
        repo.sign(type)

    override fun sendCode(code: Code.PhoneVerification) =
        repo.sendCode(code)

    override fun sendCode(code: Code.PasswordReset) =
        repo.sendCode(code)

    override fun sendCode(code: Code.VerifyEmail) =
        repo.sendCode(code)

    override fun lookup(idToken: String) =
        repo.lookup(idToken)

    override fun update(type: UpdateType) =
        repo.update(type)
}
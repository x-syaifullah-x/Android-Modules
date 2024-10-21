package id.xxx.module.auth.domain.usecase

import id.xxx.module.auth.domain.model.TypeSignIn
import id.xxx.module.auth.domain.model.TypeSignUp
import id.xxx.module.auth.domain.repository.AuthRepository

class AuthUseCaseRepositoryImpl private constructor(
    private val repo: AuthRepository
) : AuthUseCaseRepository {

    companion object {

        @Volatile
        private var _sInstance: AuthUseCaseRepository? = null

        fun getInstance(repo: AuthRepository) =
            _sInstance ?: synchronized(this) {
                _sInstance ?: AuthUseCaseRepositoryImpl(repo = repo)
                    .also { _sInstance = it }
            }
    }

    override fun signUp(type: TypeSignUp) = repo.signUp(type)

    override fun signIn(type: TypeSignIn) = repo.signIn(type)

    override fun getCurrentUser() = repo.getCurrentUser()
}
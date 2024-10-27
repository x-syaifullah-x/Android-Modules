package id.xxx.module.auth.domain.usecase

import id.xxx.module.auth.domain.model.TypeSign
import id.xxx.module.auth.domain.repository.AuthRepository

class AuthUseCaseImpl private constructor(
    private val repo: AuthRepository
) : AuthUseCase {

    companion object {

        @Volatile
        private var _sInstance: AuthUseCase? = null

        fun getInstance(repo: AuthRepository) =
            _sInstance ?: synchronized(this) {
                _sInstance ?: AuthUseCaseImpl(repo = repo)
                    .also { _sInstance = it }
            }
    }

    override fun sign(type: TypeSign) = repo.sign(type)

    override fun getCurrentUser() = repo.getCurrentUser()
}
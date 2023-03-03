package id.xxx.module.auth.activity

import id.xxx.module.auth.repository.AuthRepositoryImpl
import id.xxx.module.auth.usecase.AuthUseCaseImpl

class MainActivity : AuthActivity(AuthUseCaseImpl.getInstance(AuthRepositoryImpl.getInstance()))
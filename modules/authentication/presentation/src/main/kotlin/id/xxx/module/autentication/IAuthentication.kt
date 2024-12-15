package id.xxx.module.autentication

import id.xxx.module.auth.domain.model.AuthenticationType
import id.xxx.module.auth.domain.model.UserModel
import id.xxx.module.common.Resources
import kotlinx.coroutines.flow.Flow

interface IAuthentication {

    fun onAuthentication(type: AuthenticationType): Flow<Resources<UserModel>>
}
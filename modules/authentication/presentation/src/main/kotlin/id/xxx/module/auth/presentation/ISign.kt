package id.xxx.module.auth.presentation

import id.xxx.module.auth.domain.model.TypeSign

interface ISign {

    suspend fun onSign(type: TypeSign): SignResult
}
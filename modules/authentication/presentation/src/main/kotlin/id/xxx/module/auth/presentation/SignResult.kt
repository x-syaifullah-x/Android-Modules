package id.xxx.module.auth.presentation

sealed interface SignResult {

    class Success : SignResult

    class Error(val err: Throwable) : SignResult
}
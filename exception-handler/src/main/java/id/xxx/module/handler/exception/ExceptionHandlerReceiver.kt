package id.xxx.module.handler.exception

import java.util.concurrent.Executors

abstract class ExceptionHandlerReceiver {

    private val es by lazy { Executors.newFixedThreadPool(2) }

    open fun onError(message: String, t: Throwable) {
        es.execute { onError(t) }
        es.execute { onError(message) }
    }

    protected open fun onError(t: Throwable) {}

    protected open fun onError(message: String) {}
}
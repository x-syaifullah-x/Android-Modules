package id.xxx.module.handler.exception

import java.io.PrintWriter
import java.io.StringWriter
import kotlin.system.exitProcess

class ExceptionHandler private constructor() : Thread.UncaughtExceptionHandler {

    companion object {

        @Volatile
        private var sExceptionHandler: ExceptionHandler? = null

        fun getInstance() = sExceptionHandler ?: synchronized(this) {
            sExceptionHandler ?: ExceptionHandler().also {
                sExceptionHandler = it
                Thread.setDefaultUncaughtExceptionHandler(sExceptionHandler)
            }
        }
    }

    private val uncaughtExceptionHandler =
        Thread.getDefaultUncaughtExceptionHandler()

    private val receivers by lazy { HashSet<ExceptionHandlerReceiver>() }

    fun <T : ExceptionHandlerReceiver> register(receiver: T) =
        receivers.add(receiver)

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        try {
            throwable.printStackTrace(pw)
            receivers.forEach { re ->
                re.onError("$sw", throwable)
            }
        } finally {
            pw.close()
            sw.close()
            if (uncaughtExceptionHandler != null) {
                uncaughtExceptionHandler.uncaughtException(thread, throwable)
            } else {
                exitProcess(1)
            }
        }
    }
}
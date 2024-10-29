package runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import id.xxx.template.App
import id.xxx.template.providers.MyFileProvider
import java.io.File

class AndroidJUnitRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader, className: String, context: Context
    ): Application {
        getUriForFiletTest(context)
        return super.newApplication(cl, App::class.java.name, context)
    }

    private fun getUriForFiletTest(context: Context) {
        val directory = (File(context.cacheDir, "0/1/2/3"))
        directory.mkdirs()
        val file = File(directory, "abc.txt")
        val uri = MyFileProvider.getUriForFile(context, file)
        val fos = context.contentResolver.openOutputStream(uri, "rw")
            ?: throw NullPointerException()
        fos.write("abc".toByteArray())
        fos.flush()
        fos.close()
    }
}
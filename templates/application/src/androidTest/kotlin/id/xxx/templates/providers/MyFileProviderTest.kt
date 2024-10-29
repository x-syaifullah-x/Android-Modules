package id.xxx.templates.providers

import androidx.test.platform.app.InstrumentationRegistry
import id.xxx.template.providers.MyFileProvider
import org.junit.Assert
import org.junit.Test
import java.io.File

class MyFileProviderTest {

    @Test
    fun getUriForFile() {
        val context =
            InstrumentationRegistry.getInstrumentation().targetContext
        val directory = (File(context.cacheDir, "0/1/2/3"))
        directory.mkdirs()
        val file = File(directory, "abc.txt")
        val uri = MyFileProvider.getUriForFile(context, file)
        val fos = context.contentResolver.openOutputStream(uri, "rw")
            ?: throw NullPointerException()
        val bytes = "abc".toByteArray()
        fos.write(bytes)
        fos.flush()
        fos.close()

        val `is` = context.contentResolver.openInputStream(uri)
        val data = `is`?.readBytes() ?: byteArrayOf()
        Assert.assertEquals(String(bytes), String(data))
        `is`?.close()
    }
}
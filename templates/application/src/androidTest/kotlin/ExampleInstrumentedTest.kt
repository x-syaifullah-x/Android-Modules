import androidx.test.platform.app.InstrumentationRegistry
import id.xxx.template.providers.MyFileProvider
import org.junit.Assert
import org.junit.Test
import runner.AndroidJUnitRunner
import java.io.File

class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext =
            InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertNotNull(appContext)
    }
}
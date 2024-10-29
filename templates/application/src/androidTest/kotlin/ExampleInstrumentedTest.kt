import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test

class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext: Context =
            InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertNotNull(appContext)
    }
}
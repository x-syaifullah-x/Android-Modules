import kotlinx.coroutines.CoroutineTestRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ExampleUnitTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun addition_isCorrect() = runBlocking {
        withContext(Dispatchers.Main) {
            assertEquals(4, 4)
        }
    }
}
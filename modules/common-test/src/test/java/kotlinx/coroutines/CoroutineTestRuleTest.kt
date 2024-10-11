package kotlinx.coroutines

import org.junit.Rule
import org.junit.Test

class CoroutineTestRuleTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun run_main_thread_test() = runBlocking(Dispatchers.Main) {
        println("test_in_main_thread: ${Thread.currentThread().name}")
    }

    @Test
    fun run_io_thread_test() = runBlocking(Dispatchers.IO) {
        println("test_in_io_thread: ${Thread.currentThread().name}")
    }

    @Test
    fun run_default_thread_test() = runBlocking(Dispatchers.Default) {
        println("test_in_default_thread: ${Thread.currentThread().name}")
    }

    @Test
    fun run_unconfined_thread_test() = runBlocking(Dispatchers.Unconfined) {
        println("test_in_unconfined_thread: ${Thread.currentThread().name}")
    }
}
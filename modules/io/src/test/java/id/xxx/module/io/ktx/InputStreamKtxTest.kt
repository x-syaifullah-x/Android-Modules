package id.xxx.module.io.ktx

import kotlinx.coroutines.CoroutineTestRule
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class InputStreamKtxTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @Test
    fun test() = runBlocking {
        val bytes = ByteArray(100_000_000) { it.toByte() }

        val input = ByteArrayInputStream(bytes)

        val bufferSize = 1024
        val out = ByteArrayOutputStream(bufferSize)

        input.read(
            bufferSize,
            out,
            onRead = { count -> Assert.assertTrue(count <= bytes.size) },
            onReadComplete = {
                Assert.assertArrayEquals(bytes, it.toByteArray())
                Assert.assertEquals(out, it)
            },
            onError = { err ->
                err.printStackTrace()
            }
        )
    }

    @Test
    fun test_() = runBlocking {

        val data = "私は行ってみたい"

        val input = ByteArrayInputStream(data.toByteArray())

        val bufferSize = 3

        val out = ByteArrayOutputStream()

        input.read(
            bufferSize,
            onRead = { bytes ->
                out.write(bytes, 0, bytes.size)
            },
            onReadComplete = {
                Assert.assertEquals(data, out.toString())
            },
            onError = {
                it.printStackTrace()
            }
        )
    }
}
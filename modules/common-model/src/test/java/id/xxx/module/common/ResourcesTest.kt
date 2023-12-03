package id.xxx.module.common

import id.xxx.module.common.model.Model
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicLong

class ResourcesTest {

    companion object {

        private const val CONTENT_LENGTH = 100L
        private const val ID_RESULT = "Success"
        private const val ERROR_MESSAGE = "Error"
    }

    @Test
    fun test() {
        val rc = { result: Resources<Model<String>> ->
            when (result) {
                is Resources.Loading -> {
                    Assert.assertNotNull(result)
                    val progress = result.getProgress()
                    if (progress != null) {
                        Assert.assertTrue(progress <= CONTENT_LENGTH)
                    }
                    val length = result.getLength()
                    Assert.assertTrue(length == CONTENT_LENGTH)
                }

                is Resources.Success -> {
                    Assert.assertEquals(ID_RESULT, result.value.id)
                }

                is Resources.Failure -> {
                    Assert.assertEquals(ERROR_MESSAGE, result.value.message)
                }
            }
        }
        val thread = Thread {
            val countAtomic = AtomicLong(0)
            val lengthAtomic = AtomicLong(CONTENT_LENGTH)
            val loading = Resources.Loading(
                progress = countAtomic, length = lengthAtomic
            )
            for (i in 0..CONTENT_LENGTH) {
                countAtomic.set(i)
                rc.invoke(loading)
            }
            rc.invoke(Resources.Success(Model(ID_RESULT)))
            rc.invoke(Resources.Failure(Throwable(ERROR_MESSAGE)))
        }
        thread.start()
        thread.join()
    }
}
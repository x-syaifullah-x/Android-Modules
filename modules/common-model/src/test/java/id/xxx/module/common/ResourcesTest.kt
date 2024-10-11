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
                    val progress = result.progress
                    if (progress != null) {
                        Assert.assertTrue(progress.getCount() <= CONTENT_LENGTH)
                    }
                    val length = result.progress?.getLength()
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
            val count = AtomicLong(0)
            val loading = Resources.Loading(
                progress = Resources.Loading.Progress(count = count, length = CONTENT_LENGTH)
            )
            for (i in 0..CONTENT_LENGTH) {
                count.set(i)
                rc.invoke(loading)
            }
            rc.invoke(Resources.Success(Model(ID_RESULT)))
            rc.invoke(Resources.Failure(Throwable(ERROR_MESSAGE)))
        }
        thread.start()
        thread.join()
    }
}
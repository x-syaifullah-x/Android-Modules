package id.xxx.module.common

import id.xxx.module.common.model.Model
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicLong

class ResultTest {

    companion object {

        private const val CONTENT_LENGTH = 100L
        private const val ID_RESULT = "Success"
        private const val ERROR_MESSAGE = "Error"
    }

    @Test
    fun test() {
        val rc = { result: Result<Model<String>> ->
            when (result) {
                is Result.Loading -> {
                    val progress = result.progress
                    Assert.assertNotNull(progress)
                    if (progress == null)
                        throw NullPointerException()
                    Assert.assertTrue(progress.getCount() <= CONTENT_LENGTH)
                    Assert.assertTrue(progress.getLength() == CONTENT_LENGTH)
                }

                is Result.Success -> {
                    Assert.assertEquals(ID_RESULT, result.value.id)
                }

                is Result.Failure -> {
                    Assert.assertEquals(ERROR_MESSAGE, result.value.message)
                }
            }
        }
        val thread = Thread {
            val countAtomic = AtomicLong(0)
            val progress = Result.Loading.Progress(countAtomic, CONTENT_LENGTH)
            val loading = Result.Loading(progress)
            for (i in 0..CONTENT_LENGTH) {
                countAtomic.set(i)
                rc.invoke(loading)
            }
            rc.invoke(Result.Success(Model(ID_RESULT)))
            rc.invoke(Result.Failure(Throwable(ERROR_MESSAGE)))
        }
        thread.start()
        thread.join()
    }
}
import org.json.JSONObject
import org.junit.Assert
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 4)

        val jsonObject = JSONObject("{data:[]}")
        Assert.assertEquals(1, jsonObject.length())
        Assert.assertEquals(0, jsonObject.getJSONArray("data").length())
    }
}
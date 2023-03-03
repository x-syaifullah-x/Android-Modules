package id.xxx.module.ui.viewmodel

import id.xxx.module.viewmodel.ViewModelSingleEvent
import org.junit.Assert
import org.junit.Test

class ViewModelSingleEventTest {

    @Test
    fun test_1() {
        val viewModelSingleEvent = ViewModelSingleEvent("test")
        viewModelSingleEvent.getContentIfNotHandled()
        Assert.assertEquals("test", viewModelSingleEvent.peekContent())
        Assert.assertEquals(null, viewModelSingleEvent.getContentIfNotHandled())
        Assert.assertEquals(true, viewModelSingleEvent.hasBeenHandled)
    }

    @Test
    fun test_2() {
        val viewModelSingleEvent = ViewModelSingleEvent("test")
        Assert.assertEquals("test", viewModelSingleEvent.peekContent())
        Assert.assertEquals("test", viewModelSingleEvent.getContentIfNotHandled())
        Assert.assertEquals(true, viewModelSingleEvent.hasBeenHandled)
    }
}
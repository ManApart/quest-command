package system

import org.junit.Assert
import org.junit.Test

class BodyManagerTest {

    @Test
    fun bodyManagerLoadsBodies(){
        Assert.assertTrue(BodyManager.bodyExists("Human"))
    }

}
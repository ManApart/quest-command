package system

import org.junit.Assert
import org.junit.Test
import system.body.BodyManager
import system.body.BodyParser

class BodyManagerTest {

    @Test
    fun bodyManagerLoadsBodies(){
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        Assert.assertTrue(BodyManager.bodyExists("Human"))
    }

}
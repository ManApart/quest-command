package system

import core.DependencyInjector
import core.body.NONE
import org.junit.Assert
import org.junit.Test
import core.body.BodyManager
import core.body.BodyParser
import kotlin.test.assertNotNull

class BodyManagerTest {

    @Test
    fun NONEBodyLoads() {
        assertNotNull(NONE)
    }

    @Test
    fun bodyManagerLoadsBodies(){
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        Assert.assertTrue(BodyManager.bodyExists("Human"))
    }

}
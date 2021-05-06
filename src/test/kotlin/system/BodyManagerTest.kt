package system

import core.DependencyInjector
import core.body.NONE
import org.junit.Assert
import org.junit.Test
import core.body.BodyManager
import traveling.location.location.LocationParser
import kotlin.test.assertNotNull

class BodyManagerTest {

    @Test
    fun `NONE Body Loads`() {
        assertNotNull(NONE)
    }

    @Test
    fun bodyManagerLoadsBodies(){
        val bodyParser = BodyFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, bodyParser)
        BodyManager.reset()

        Assert.assertTrue(BodyManager.bodyExists("Human"))
    }

}
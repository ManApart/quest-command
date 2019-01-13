package system

import core.gameState.body.ProtoBody
import org.junit.Assert
import org.junit.Test
import system.body.BodyManager
import system.body.BodyParser
import kotlin.test.fail

class BodyManagerTest {

    @Test
    fun bodyManagerLoadsBodies(){
        val bodyParser = BodyFakeParser(listOf(ProtoBody("Human")))
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        Assert.assertTrue(BodyManager.bodyExists("Human"))
    }

}
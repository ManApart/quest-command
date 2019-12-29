package combat

import combat.attack.AttackCommand
import combat.attack.StartAttackEvent
import core.DependencyInjector
import core.GameManager
import core.GameState
import core.ai.AIFakeParser
import core.ai.AIManager
import core.ai.AIParser
import core.ai.behavior.BehaviorManager
import core.ai.behavior.BehaviorParser
import core.body.BodyManager
import core.body.BodyParser
import core.events.EventManager
import core.target.Target
import org.junit.Before
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.location.LocationFakeParser
import traveling.location.LocationManager
import traveling.location.LocationParser
import traveling.scope.ScopeManager
import kotlin.test.assertEquals

class AttackCommandTest {
    private val command = AttackCommand()

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser.parserWithFakePlayer()
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val aiParser = AIFakeParser()
        DependencyInjector.setImplementation(AIParser::class.java, aiParser)
        AIManager.reset()

        val behaviorParser = BehaviorFakeParser()
        DependencyInjector.setImplementation(BehaviorParser::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        EventManager.clear()
        ScopeManager.reset()

        GameState.player = GameManager.newPlayer()
    }

    @Test
    fun attackCreatureWithoutDirection() {
        val rat = Target("Rat", body = "human")
        ScopeManager.getScope().addTarget(rat)

        command.execute("sl", "rat".split(" "))
        val event = EventManager.getUnexecutedEvents()[0] as StartAttackEvent
        assertEquals(rat, event.target.target)
    }
}
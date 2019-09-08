package combat

import combat.attack.AttackCommand
import combat.attack.StartAttackEvent
import core.gameState.*
import core.gameState.Target
import interact.scope.ScopeManager
import org.junit.Before
import org.junit.Test
import system.BehaviorFakeParser
import system.BodyFakeParser
import system.DependencyInjector
import system.EventManager
import system.behavior.BehaviorManager
import system.behavior.BehaviorParser
import system.body.BodyManager
import system.body.BodyParser
import system.location.LocationFakeParser
import system.location.LocationManager
import system.location.LocationParser
import kotlin.test.assertEquals

class AttackCommandTest {
    private val command = AttackCommand()

    @Before
    fun setup() {
        val bodyParser = BodyFakeParser.parserWithFakePlayer()
        DependencyInjector.setImplementation(BodyParser::class.java, bodyParser)
        BodyManager.reset()

        val behaviorParser = BehaviorFakeParser()
        DependencyInjector.setImplementation(BehaviorParser::class.java, behaviorParser)
        BehaviorManager.reset()

        val locationParser = LocationFakeParser()
        DependencyInjector.setImplementation(LocationParser::class.java, locationParser)
        LocationManager.reset()

        EventManager.clear()
        ScopeManager.reset()

        GameState.player = Player()
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
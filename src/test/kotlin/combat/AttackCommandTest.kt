package combat

import combat.attack.AttackCommand
import combat.attack.StartAttackEvent
import core.GameManager
import core.GameState
import core.events.EventManager
import core.target.Target
import createMockedGame
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AttackCommandTest {
    private val command = AttackCommand()

    @Before
    fun setup() {
        createMockedGame()
        EventManager.clear()

        GameState.player = GameManager.newPlayer()
    }

    @Test
    fun attackCreatureWithoutDirection() {
        val rat = Target("Rat", bodyName = "human")
        GameState.currentLocation().addTarget(rat)

        command.execute("sl", "rat".split(" "))
        val event = EventManager.getUnexecutedEvents()[0] as StartAttackEvent
        assertEquals(rat, event.target.target)
    }
}

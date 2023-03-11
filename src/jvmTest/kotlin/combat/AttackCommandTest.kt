package combat

import combat.attack.AttackCommand
import combat.attack.AttackEvent
import core.GameManager
import core.events.EventManager
import core.thing.thing
import createMockedGame
import kotlinx.coroutines.runBlocking

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AttackCommandTest {
    private val command = AttackCommand()

    @BeforeTest
    fun setup() {
        createMockedGame()
        EventManager.clear()
    }

    @Test
    fun attackCreatureWithoutDirection() {
        runBlocking {
            val player = GameManager.newPlayer()
            val rat = thing("Rat") {
                body("human")
            }.build()
            player.thing.currentLocation().addThing(rat)

            command.execute(player, "sl", "rat".split(" "))
            val event = EventManager.getUnexecutedEvents()[0] as AttackEvent
            assertEquals(rat, event.aim.thing)
        }
    }
}

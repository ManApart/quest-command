package system.connection

import core.GameState.player
import core.events.EventManager
import createMockedGame
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ConnectionCommandTest {
    private val command = ConnectCommand()

    @Before
    fun setup() {
        createMockedGame()
    }

    @Test
    fun fullCommand() {
        command.execute(player, "connect", "Bob 127.0.0.0 8090".split(" "))

        val expected = ConnectEvent(player, "Bob", "http://127.0.0.0", "8090")

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertEquals(expected, events.first())
    }

    @Test
    fun noPort() {
        command.execute(player, "connect", "Charles google.com".split(" "))

        val expected = ConnectEvent(player, "Charles", "http://google.com")

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertEquals(expected, events.first())
    }

    @Test
    fun defaults() {
        command.execute(player, "connect", "Jim".split(" "))

        val expected = ConnectEvent(player, "Jim", "http://localhost", "8080")

        val events = EventManager.getUnexecutedEvents()
        assertEquals(1, events.size)
        assertEquals(expected, events.first())
    }
}

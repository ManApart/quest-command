package system.connection

import core.GameState.player
import core.events.EventManager
import createMockedGame
import kotlin.test.Test
import kotlin.test.AfterTest
import kotlin.test.BeforeTest


import kotlin.test.assertEquals

class ConnectionCommandTest {
    private val command = ConnectCommand()

    @BeforeTest
    fun setup() {
        createMockedGame()
    }

    @AfterTest
    fun teardown() {
        EventManager.clear()
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
    fun noName() {
        command.execute(player, "connect", "google.com 8081".split(" "))

        val expected = ConnectEvent(player, "Player", "http://google.com", "8081")

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

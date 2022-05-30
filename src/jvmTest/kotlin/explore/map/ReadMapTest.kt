package explore.map

import core.GameState
import core.Player
import core.history.GameLogger
import core.thing.Thing
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import traveling.location.Connection
import traveling.location.location.LocationPoint
import traveling.location.network.LocationNode
import traveling.position.Vector

class ReadMapTest {
    val player = Player("Bob", Thing("Bob"))

    @Before
    fun setup() {
        GameState.putPlayer(player)
        GameLogger.reset()
        GameLogger.track(player)
        player.mind.forgetShortTermMemory()
        player.mind.forgetLongTermMemory()
    }

    @Test
    fun noNeighbors() {
        val thing = LocationNode("My Place")

        val event = ReadMapEvent(player, thing)

        val listener = ReadMap()
        listener.execute(event)
        val actual = GameLogger.getHistory(player).getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It has no known neighbors.", actual)
    }

    @Test
    fun aSingleNeighborIsProperlyDisplayedWithDirection() {
        val thing = LocationNode("My Place")
        thing.addConnection(
            Connection(
                LocationPoint(thing, Vector(0, 10, 0)),
                LocationPoint(LocationNode("Destination"))
            )
        )
        thing.getNeighborConnections().forEach { player.thing.mind.discover(it.destination.location) }
        val event = ReadMapEvent(player, thing)

        val listener = ReadMap()
        listener.execute(event)
        val actual = GameLogger.getHistory(player).getLastOutput()
        Assert.assertEquals(
            "My Place is a part of Wilderness. It is neighbored by:\n" +
                    "  Name         Distance  Direction Path  \n" +
                    "  Destination  10        N               \n", actual
        )
    }

    @Test
    fun neighborsAreProperlyDisplayedWithDirection() {
        val thing = LocationNode("My Place")

        with(thing) {
            addConnection(Vector(0, 10, 0), "north")
            addConnection(Vector(0, -10, 0), "south")
            addConnection(Vector(10, 0, 0), "east")
            addConnection(Vector(-10, 0, 0), "west")
            getNeighborConnections().forEach { player.thing.mind.discover(it.destination.location) }
        }

        val event = ReadMapEvent(player, thing)
        val listener = ReadMap()
        listener.execute(event)
        val actual = GameLogger.getHistory(player).getLastOutput()
        Assert.assertEquals(
            "My Place is a part of Wilderness. It is neighbored by:\n" +
                    "  Name   Distance  Direction Path  \n" +
                    "  north  10        N               \n" +
                    "  south  10        S               \n" +
                    "  east   10        E               \n" +
                    "  west   10        W               \n", actual
        )
    }

    private fun LocationNode.addConnection(vector: Vector, locationName: String) {
        addConnection(Connection(LocationPoint(this, vector), LocationPoint(LocationNode(locationName))))
    }

}
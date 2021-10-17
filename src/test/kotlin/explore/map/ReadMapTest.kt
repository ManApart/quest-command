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
    val player = Player(1, Thing("Bob"))

    @Before
    fun setup(){
        GameState.player = player
        GameLogger.reset()
        GameLogger.track(player)
        player.knownLocations.clear()
    }

    @Test
    fun noNeighbors(){
        val thing = LocationNode("My Place")

        val event = ReadMapEvent(player, thing)

        val listener = ReadMap()
        listener.execute(event)
        val actual = GameLogger.getHistory(player).getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It has no known neighbors.", actual)
    }

    @Test
    fun aSingleNeighborIsProperlyDisplayedWithDirection(){
        val thing = LocationNode("My Place")
        thing.addConnection(Connection(LocationPoint(thing), LocationPoint(LocationNode("Destination")), Vector(0, 10, 0)))
        thing.getNeighborConnections().forEach { player.discover(it.destination.location) }
        val event = ReadMapEvent(player, thing)

        val listener = ReadMap()
        listener.execute(event)
        val actual = GameLogger.getHistory(player).getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It is neighbored by:\n" +
                "  Name         Distance  Direction Path  \n" +
                "  Destination  10        N               \n", actual)
    }

    @Test
    fun neighborsAreProperlyDisplayedWithDirection(){
        val thing = LocationNode("My Place")
        val thingPoint = LocationPoint(thing)
        thing.addConnection(Connection(thingPoint, LocationPoint(LocationNode("north")), Vector(0, 10, 0)))
        thing.addConnection(Connection(thingPoint, LocationPoint(LocationNode("south")), Vector(0, -10, 0)))
        thing.addConnection(Connection(thingPoint, LocationPoint(LocationNode("east")), Vector(10, 0, 0)))
        thing.addConnection(Connection(thingPoint, LocationPoint(LocationNode("west")), Vector(-10, 0, 0)))
        thing.getNeighborConnections().forEach { player.discover(it.destination.location) }
        val event = ReadMapEvent(player, thing)

        val listener = ReadMap()
        listener.execute(event)
        val actual = GameLogger.getHistory(player).getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It is neighbored by:\n" +
                "  Name   Distance  Direction Path  \n" +
                "  north  10        N               \n" +
                "  south  10        S               \n" +
                "  east   10        E               \n" +
                "  west   10        W               \n", actual)
    }
}
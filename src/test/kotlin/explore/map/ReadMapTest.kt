package explore.map

import core.Player
import core.history.GameLogger
import core.target.Target
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import traveling.location.Connection
import traveling.location.location.LocationPoint
import traveling.location.network.LocationNode
import traveling.position.Vector

class ReadMapTest {
    val player = Player(1, Target("Bob"))

    @Before
    fun setup(){
        GameLogger.reset()
        GameLogger.track(player.target)
    }

    @Test
    fun noNeighbors(){
        val target = LocationNode("My Place")

        val event = ReadMapEvent(player.target, target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = GameLogger.getHistory(player.target).getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It has no known neighbors.", actual)
    }

    @Test
    fun aSingleNeighborIsProperlyDisplayedWithDirection(){
        val target = LocationNode("My Place")
        target.addConnection(Connection(LocationPoint(target), LocationPoint(LocationNode("Destination")), Vector(0, 10, 0)))
        player.knownLocations.clear()
        target.getNeighborConnections().forEach { player.discover(it.destination.location) }
        val event = ReadMapEvent(player.target, target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = GameLogger.getHistory(player.target).getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It is neighbored by:\n" +
                "  Name         Distance  Direction Path  \n" +
                "  Destination  10        N               \n", actual)
    }

    @Test
    fun neighborsAreProperlyDisplayedWithDirection(){
        val target = LocationNode("My Place")
        val targetPoint = LocationPoint(target)
        target.addConnection(Connection(targetPoint, LocationPoint(LocationNode("north")), Vector(0, 10, 0)))
        target.addConnection(Connection(targetPoint, LocationPoint(LocationNode("south")), Vector(0, -10, 0)))
        target.addConnection(Connection(targetPoint, LocationPoint(LocationNode("east")), Vector(10, 0, 0)))
        target.addConnection(Connection(targetPoint, LocationPoint(LocationNode("west")), Vector(-10, 0, 0)))
        player.knownLocations.clear()
        target.getNeighborConnections().forEach { player.discover(it.destination.location) }
        val event = ReadMapEvent(player.target, target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = GameLogger.getHistory(player.target).getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It is neighbored by:\n" +
                "  Name   Distance  Direction Path  \n" +
                "  north  10        N               \n" +
                "  south  10        S               \n" +
                "  east   10        E               \n" +
                "  west   10        W               \n", actual)
    }
}
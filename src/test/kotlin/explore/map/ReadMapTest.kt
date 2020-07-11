package explore.map

import traveling.position.Vector
import traveling.location.Connection
import traveling.location.location.LocationNode
import traveling.location.location.LocationPoint
import core.history.ChatHistory
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ReadMapTest {

    @Before
    fun setup(){
        ChatHistory.reset()
    }

    @Test
    fun noNeighbors(){
        val target = LocationNode("My Place")
        val event = ReadMapEvent(target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = ChatHistory.getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It has no known neighbors.", actual)
    }

    @Test
    fun aSingleNeighborIsProperlyDisplayedWithDirection(){
        val target = LocationNode("My Place")
        target.addConnection(Connection(LocationPoint(target), LocationPoint(LocationNode("Destination")), Vector(0, 10, 0)))
        val event = ReadMapEvent(target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = ChatHistory.getLastOutput()
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
        val event = ReadMapEvent(target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = ChatHistory.getLastOutput()
        Assert.assertEquals("My Place is a part of Wilderness. It is neighbored by:\n" +
                "  Name   Distance  Direction Path  \n" +
                "  north  10        N               \n" +
                "  south  10        S               \n" +
                "  east   10        E               \n" +
                "  west   10        W               \n", actual)
    }
}
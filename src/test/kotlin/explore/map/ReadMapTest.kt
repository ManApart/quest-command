package explore.map

import core.gameState.Position
import core.gameState.location.LocationLink
import core.gameState.location.LocationNode
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
    fun aSingleNeighborIsProperlyDisplayedWithDirection(){
        val target = LocationNode("My Place")
        target.addLink(LocationLink(target, LocationNode("Destination"), Position(0,10,0)))
        val event = ReadMapEvent(target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = ChatHistory.getLastOutput()
        Assert.assertEquals("My Place is neighbored by Destination (NORTH).", actual)
    }

    @Test
    fun neighborsAreProperlyDisplayedWithDirection(){
        val target = LocationNode("My Place")
        target.addLink(LocationLink(target, LocationNode("north"), Position(0,10,0)))
        target.addLink(LocationLink(target, LocationNode("south"), Position(0,-10,0)))
        target.addLink(LocationLink(target, LocationNode("east"), Position(10,0,0)))
        target.addLink(LocationLink(target, LocationNode("west"), Position(-10,0,0)))
        val event = ReadMapEvent(target)

        val listener = ReadMap()
        listener.execute(event)
        val actual = ChatHistory.getLastOutput()
        Assert.assertEquals("My Place is neighbored by north (NORTH), south (SOUTH), east (EAST), west (WEST).", actual)
    }
}
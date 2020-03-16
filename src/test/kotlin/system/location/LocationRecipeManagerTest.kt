package system.location

import traveling.location.location.LocationNode
import traveling.location.ProtoConnection
import core.utility.NameSearchableList
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import core.DependencyInjector
import traveling.location.location.LocationManager
import traveling.location.location.LocationParser

class LocationRecipeManagerTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setup() {
            //Run before other tests so object is initialized and we're testing a fresh clear each time
            val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(listOf()))
            DependencyInjector.setImplementation(LocationParser::class.java, fakeParser)
            LocationManager.getNetworks()
        }
    }

    @Test
    fun linksCreateNeighborsIfTheyDoNotExist() {
        val neighborLink = ProtoConnection(name = "neighbor")
        val source = LocationNode("source", protoConnections = mutableListOf(neighborLink))
        val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(source))

        DependencyInjector.setImplementation(LocationParser::class.java, fakeParser)
        LocationManager.reset()

        Assert.assertEquals(source, LocationManager.getNetwork("Wilderness").getLocationNode(source.name))
        Assert.assertEquals(neighborLink.connection.location, LocationManager.getNetwork("Wilderness").findLocation(neighborLink.connection.location).name)
    }

    @Test
    fun noDuplicateNodesOrLinks() {
        val neighborExistsName = "neighbor exists"
        val neighborDoesNotExistsName = "neighbor doesn't exists"

        val source = LocationNode("source", protoConnections = mutableListOf(ProtoConnection(name = neighborExistsName), ProtoConnection(name = neighborDoesNotExistsName)))
        val neighborExists = LocationNode(neighborExistsName)
        val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(listOf(source, neighborExists)))

        DependencyInjector.setImplementation(LocationParser::class.java, fakeParser)
        LocationManager.reset()

        val neighborDoesNotExists = LocationManager.getNetwork("Wilderness").findLocation(neighborDoesNotExistsName)

        //No Duplicate Nodes
        Assert.assertEquals(source, LocationManager.getNetwork("Wilderness").getLocationNode(source.name))
        Assert.assertEquals(neighborExists, LocationManager.getNetwork("Wilderness").findLocation(neighborExists.name))
        Assert.assertEquals(neighborDoesNotExistsName, neighborDoesNotExists.name)
        Assert.assertEquals(3, LocationManager.getNetwork("Wilderness").countLocationNodes())

        //No Duplicate Links
        Assert.assertEquals(2, source.getNeighborConnections().size)
        Assert.assertEquals(1, neighborExists.getNeighborConnections().size)
        Assert.assertEquals(1, neighborDoesNotExists.getNeighborConnections().size)
    }

    @Test
    fun oneWayLinksDontLinkBack() {

        val neighbor = LocationNode("neighbor")
        val source = LocationNode("source", protoConnections = mutableListOf(ProtoConnection(name = neighbor.name, oneWay = true)))
        val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(listOf(source, neighbor)))

        DependencyInjector.setImplementation(LocationParser::class.java, fakeParser)
        LocationManager.reset()

        Assert.assertEquals(source, LocationManager.getNetwork("Wilderness").getLocationNode(source.name))
        Assert.assertEquals(neighbor, LocationManager.getNetwork("Wilderness").getLocationNode(neighbor.name))
        Assert.assertEquals(1, source.getNeighborConnections().size)
        Assert.assertEquals(0, neighbor.getNeighborConnections().size)

    }

    @Test
    fun locationNodesCreateLocationsThatDontExist() {
        val source = LocationNode("source")
        val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(source))

        DependencyInjector.setImplementation(LocationParser::class.java, fakeParser)
        LocationManager.reset()

        Assert.assertEquals(source.name, source.getLocationRecipe().name)
    }


}
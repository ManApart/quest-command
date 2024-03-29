package system.location

import core.DependencyInjector
import createMockedGame
import kotlin.test.Test


import traveling.location.location.LocationManager
import traveling.location.location.LocationsCollection
import traveling.location.location.LocationsMock
import traveling.location.network.*
import kotlin.test.BeforeTest
import kotlin.test.assertEquals


class LocationRecipeManagerTest {

    @BeforeTest
    fun setup() {
        //Run before other tests so object is initialized and we're testing a fresh clear each time
//            val fakeParser = LocationFakeParser(locationNodes = NameSearchableList(listOf()))
        val networksMock = NetworksMock(networks {  })
        DependencyInjector.setImplementation(NetworksCollection::class, networksMock)
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
        LocationManager.getNetworks()
        createMockedGame()
    }

    @Test
    fun linksCreateNeighborsIfTheyDoNotExist() {
        val neighbor = "neighbor"
        val source = "source"
        val networksMock = NetworksMock(networks {
            network(DEFAULT_NETWORK.name){
                locationNode(source){
                    connection(neighbor)
                }
            }
        })
        DependencyInjector.setImplementation(NetworksCollection::class, networksMock)
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
        LocationManager.reset()

        assertEquals(source, LocationManager.getNetwork("Wilderness").getLocationNode(source).name)
        assertEquals(neighbor, LocationManager.getNetwork("Wilderness").findLocation(neighbor).name)
    }

    @Test
    fun noDuplicateNodesOrLinks() {
        val sourceName = "source"
        val neighborExistsName = "neighbor exists"
        val neighborDoesNotExistsName = "neighbor doesn't exists"

        val networksMock = NetworksMock(networks {
            network(DEFAULT_NETWORK.name){
                locationNode(sourceName){
                    connection(neighborExistsName)
                    connection(neighborDoesNotExistsName)
                }
                locationNode(neighborExistsName)
            }
        })
        DependencyInjector.setImplementation(NetworksCollection::class, networksMock)
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
        LocationManager.reset()

        val network = LocationManager.getNetwork("Wilderness")
        val source = network.findLocation(sourceName)
        val neighborExists = network.findLocation(neighborExistsName)
        val neighborDoesNotExists = network.findLocation(neighborDoesNotExistsName)

        //No Duplicate Nodes
        assertEquals(sourceName, source.name)
        assertEquals(neighborExistsName, neighborExists.name)
        assertEquals(neighborDoesNotExistsName, neighborDoesNotExists.name)
        assertEquals(3, network.countLocationNodes())

        //No Duplicate Links
        assertEquals(2, source.getNeighborConnections().size)
        assertEquals(1, neighborExists.getNeighborConnections().size)
        assertEquals(1, neighborDoesNotExists.getNeighborConnections().size)
    }

    @Test
    fun oneWayLinksDontLinkBack() {
        val sourceName = "source"
        val neighborName = "neighbor"

        val networksMock = NetworksMock(networks {
            network(DEFAULT_NETWORK.name){
                locationNode(sourceName){
                    connection(neighborName){
                        oneWay(true)
                    }
                }
                locationNode("neighbor")
            }
        })
        DependencyInjector.setImplementation(NetworksCollection::class, networksMock)
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
        LocationManager.reset()

        val network = LocationManager.getNetwork("Wilderness")
        val source = network.findLocation(sourceName)
        val neighbor = network.findLocation(neighborName)

        assertEquals(sourceName, source.name)
        assertEquals(neighborName, neighbor.name)
        assertEquals(1, source.getNeighborConnections().size)
        assertEquals(0, neighbor.getNeighborConnections().size)

    }

    @Test
    fun locationNodesCreateLocationsThatDontExist() {
        val source = LocationNode("source")

        val networksMock = NetworksMock(networks {
            network(DEFAULT_NETWORK.name){
                locationNode("source")
            }
        })
        DependencyInjector.setImplementation(NetworksCollection::class, networksMock)
        DependencyInjector.setImplementation(LocationsCollection::class, LocationsMock())
        LocationManager.reset()

        assertEquals(source.name, source.recipe.name)
    }


}
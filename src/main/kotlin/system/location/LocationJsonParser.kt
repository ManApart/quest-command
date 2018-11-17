package system.location

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.location.Location
import core.gameState.location.LocationNode
import core.utility.NameSearchableList

class LocationJsonParser : LocationParser {

    override fun loadLocations(): NameSearchableList<Location> {
        val json = this::class.java.getResourceAsStream("/data/location/Locations.json")
        return jacksonObjectMapper().readValue(json)
    }

    override fun loadLocationNodes(): List<LocationNode> {
        val json = this::class.java.getResourceAsStream("/data/location/LocationLinks.json")
        return jacksonObjectMapper().readValue(json)
    }
}
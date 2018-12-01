package system.location

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.location.Location
import core.gameState.location.LocationNode
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class LocationJsonParser : LocationParser {
    private fun parseLocationFile(path: String): List<Location> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))
    private fun parseLocationNodeFile(path: String): List<LocationNode> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadLocations(): NameSearchableList<Location> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/content/location/locations", ::parseLocationFile))
    }

    override fun loadLocationNodes(): List<LocationNode> {
        return JsonDirectoryParser.parseDirectory("/data/content/location/location-nodes", ::parseLocationNodeFile)
    }
}
package traveling.location.location

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class LocationJsonParser : LocationParser {
    private fun parseLocationFile(path: String): List<LocationRecipe> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))
    private fun parseLocationNodeFile(path: String): List<LocationNode> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadLocations(path: String): NameSearchableList<LocationRecipe> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory(path, ::parseLocationFile))
    }

    override fun loadLocationNodes(path: String): List<LocationNode> {
        return JsonDirectoryParser.parseDirectory(path, ::parseLocationNodeFile)
    }
}
package core.body

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import traveling.location.location.LocationNode
import core.utility.JsonDirectoryParser

class BodyJsonParser : BodyParser {
    private fun parseBodiesFile(path: String): List<LocationNode> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))
    private fun parsePartsFile(path: String): List<BodyPart> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadBodyParts(): List<BodyPart> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/bodies/parts", ::parsePartsFile)
    }

    override fun loadBodies(): List<LocationNode> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/bodies/bodies", ::parseBodiesFile)
    }


}
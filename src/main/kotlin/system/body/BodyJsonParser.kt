package system.body

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.body.BodyPart
import core.gameState.body.ProtoBody
import core.utility.JsonDirectoryParser

class BodyJsonParser : BodyParser {
    private fun parseBodiesFile(path: String): List<ProtoBody> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))
    private fun parsePartsFile(path: String): List<BodyPart> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadBodyParts(): List<BodyPart> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/bodies/parts", ::parsePartsFile)
    }

    override fun loadBodies(): List<ProtoBody> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/bodies/bodies", ::parseBodiesFile)
    }


}
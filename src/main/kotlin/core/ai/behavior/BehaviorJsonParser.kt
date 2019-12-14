package core.ai.behavior

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser

class BehaviorJsonParser : BehaviorParser {
    private fun parseFile(path: String): List<BehaviorBase> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadBehaviors(): List<BehaviorBase> {
        return JsonDirectoryParser.parseDirectory("/data/generated/content/behaviors", ::parseFile)
    }
}
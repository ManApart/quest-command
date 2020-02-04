package status.conditions

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class ConditionJsonParser : ConditionParser {
    private fun parseFile(path: String): List<ConditionRecipe> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadConditions(): NameSearchableList<ConditionRecipe> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/conditions", ::parseFile))
    }
}
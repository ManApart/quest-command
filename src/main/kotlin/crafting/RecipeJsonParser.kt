package crafting

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

class RecipeJsonParser : RecipeParser {
    private fun parseFile(path: String): List<Recipe> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    override fun loadRecipes(): NameSearchableList<Recipe> {
        return NameSearchableList(JsonDirectoryParser.parseDirectory("/data/generated/content/recipes", ::parseFile))
    }

}
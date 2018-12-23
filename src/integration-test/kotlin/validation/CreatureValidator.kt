package validation

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser

class CreatureValidator {
    private val creatureMap by lazy { JsonDirectoryParser.parseDirectory("/data/generated/content/creatures", ::parseFile) }
    private val itemMap by lazy { JsonDirectoryParser.parseDirectory("/data/generated/content/items", ::parseFile) }
    private fun parseFile(path: String): List<MutableMap<String, Any>> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    fun validate(): Int {
        return noDuplicateNames() +
                itemsThatCreaturesHaveExist()
    }

    private fun noDuplicateNames(): Int {
        var warnings = 0
        val names = mutableListOf<String>()
        creatureMap.forEach { creature ->
            val name = creature["name"] as String
            if (names.contains(name)) {
                println("WARN: Creature '$name' has a duplicate name.")
                warnings++
            } else {
                names.add(name)
            }
        }
        return warnings
    }

    private fun itemsThatCreaturesHaveExist(): Int {
        var warnings = 0
        creatureMap.forEach { creature ->
            if (creature.containsKey("items")) {
                @Suppress("UNCHECKED_CAST")
                val items = creature["items"] as List<String>
                items.forEach { item ->
                    if (!itemExists(item)) {
                        println("WARN: Creature '${creature["name"]}' has a non existent item '$item'.")
                        warnings++
                    }
                }
            }
        }
        return warnings
    }

    private fun itemExists(item: String): Boolean {
        return itemMap.any { (it["name"] as String).toLowerCase() == item.toLowerCase() }
    }


}
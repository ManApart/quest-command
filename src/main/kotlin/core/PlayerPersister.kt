package core

import core.target.persist
import crafting.RecipeManager
import system.persistance.clean
import system.persistance.cleanPathToFile
import system.persistance.loadMap
import system.persistance.writeSave
import traveling.location.Network

fun persist(dataObject: Player, path: String) {
    val prefix = clean(path, dataObject.target.name)
    val saveName = cleanPathToFile("json", prefix)
    val data = mutableMapOf<String, Any>("version" to 1)
    data["id"] = dataObject.id
    data["recipes"] = dataObject.knownRecipes.map { it.name }
    data["knownLocations"] = dataObject.knownLocations
    data["target"] = persist(dataObject.target, path)

    writeSave(path, saveName, data)
}

@Suppress("UNCHECKED_CAST")
fun load(path: String, parentLocation: Network? = null): Player {
    val data = loadMap(path)
    val id = data["id"] as Int
    val recipes = data["recipes"] as List<String>
    val target = core.target.readFromData(data["target"] as Map<String, Any>, path, parentLocation)
    val knownLocations = data["knownLocations"] as Map<String, List<String>>

    val player = Player(id, target)
    recipes.forEach { recipeName ->
        player.knownRecipes.add(RecipeManager.getRecipe(recipeName))
    }
    knownLocations.entries.forEach { (network, locations) ->
        player.knownLocations.putIfAbsent(network, mutableSetOf())
        locations.forEach { location ->
            player.knownLocations[network]?.add(location)
        }
    }

    return player
}
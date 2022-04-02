package core

import core.thing.ThingP
import crafting.RecipeManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import system.persistance.clean
import system.persistance.cleanPathToFile
import system.persistance.loadFromPath
import system.persistance.writeSave
import traveling.location.Network

fun persist(dataObject: Player, path: String) {
    val prefix = clean(path, dataObject.thing.name)
    val saveName = cleanPathToFile("json", prefix)
    val playerP = PlayerP(dataObject)
    val data = Json.encodeToString(playerP)

    writeSave(path, saveName, data)
    playerP.persistReferences(path)
}

@kotlinx.serialization.Serializable
data class PlayerP(
    val id: Int,
    val recipes: List<String>,
    val knownLocations: Map<String, Set<String>>,
    val thing: ThingP,
) {
    constructor(b: Player) : this(b.id, b.knownRecipes.map { it.name }, b.knownLocations, ThingP(b.thing))

    fun parsed(path: String, parentLocation: Network? = null): Player {

        val player = Player(id, thing.parsed(path, parentLocation))
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

    fun persistReferences(path: String) {
        val prefix = clean(path, thing.name)
        thing.persistReferences(prefix)
    }
}
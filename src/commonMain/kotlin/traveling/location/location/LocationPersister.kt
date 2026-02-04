package traveling.location.location

import core.properties.PropertiesP
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import crafting.material.MaterialManager
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import system.mapper
import system.persistance.*
import traveling.location.network.LocationNode

suspend fun persist(dataObject: Location, path: String, ignoredThings: List<Thing> = listOf()) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile(".json", prefix)
    val locationJson = mapper.encodeToString(LocationP(dataObject))
    writeSave(path, saveName, locationJson)

    dataObject.getActivators().forEach { core.thing.persistToDisk(it, clean(prefix, "activators")) }
    dataObject.getCreatures().filter { !it.isPlayer() }.forEach { core.thing.persistToDisk(it, clean(prefix, "creatures")) }
    dataObject.getItems().filter { !ignoredThings.contains(it) }.forEach { core.thing.persistToDisk(it, clean(prefix, "items")) }
    dataObject.getOther().forEach { core.thing.persistToDisk(it, clean(prefix, "other")) }
    //Persist weather (conditional string)
    //Persist last weather change

}

suspend fun load(path: String, locationNode: LocationNode): Location {
    val json: LocationP = mapper.decodeFromString(File(path).readText())
    return json.parsed(path, locationNode)
}

private suspend fun getThings(folderPath: String, folderName: String, parent: LocationNode): NameSearchableList<Thing> {
    return getFiles(clean(folderPath, folderName)).map { core.thing.loadFromDisk(it.path, parent.network) }.toNameSearchableList()
}


@kotlinx.serialization.Serializable
data class LocationP(
    val name: String,
    val materialName: String,
    val properties: PropertiesP = PropertiesP(),
) {
    constructor(b: Location) : this(b.name, b.material.name, PropertiesP(b.properties))

    suspend fun parsed(path: String, locationNode: LocationNode): Location {
        val folderPath = path.removeSuffix(".json")

        val activators = getThings(folderPath, "activators", locationNode)
        val creatures = getThings(folderPath, "creatures", locationNode)
        val items = getThings(folderPath, "items", locationNode)
        val other = getThings(folderPath, "other", locationNode)
        val recipe = locationNode.recipe.copy(material = materialName)
        return Location(locationNode, activators, creatures, items, other, properties.parsed(), recipe).apply { changeWeatherIfEnoughTimeHasPassed() }
    }
}

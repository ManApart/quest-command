package traveling.location.location

import core.properties.PropertiesP
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.toNameSearchableList
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import system.persistance.*
import traveling.location.network.LocationNode

fun persist(dataObject: Location, path: String, ignoredThings: List<Thing> = listOf()) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile(".json", prefix)
    val locationJson = Json.encodeToString(LocationP(dataObject))
    writeSave(path, saveName, locationJson)

    dataObject.getActivators().forEach { core.thing.persistToDisk(it, clean(prefix, "activators")) }
    dataObject.getCreatures().filter { !it.isPlayer() }.forEach { core.thing.persistToDisk(it, clean(prefix, "creatures")) }
    dataObject.getItems().filter { !ignoredThings.contains(it) }.forEach { core.thing.persistToDisk(it, clean(prefix, "items")) }
    dataObject.getOther().forEach { core.thing.persistToDisk(it, clean(prefix, "other")) }
    //Persist weather
    //Persist last weather change

}

fun load(path: String, locationNode: LocationNode): Location {
    val json: LocationP = Json.decodeFromString(File(path).readText())
    return json.parsed(path, locationNode)
}

private fun getThings(folderPath: String, folderName: String, parent: LocationNode): NameSearchableList<Thing> {
    return getFiles(clean(folderPath, folderName)).map { core.thing.loadFromDisk(it.path, parent.network) }.toNameSearchableList()
}


@kotlinx.serialization.Serializable
data class LocationP(
    val name: String,
    val properties: PropertiesP,
    ){
    constructor(b: Location): this(b.name, PropertiesP(b.properties))

    fun parsed(path: String, locationNode: LocationNode): Location {
        val folderPath = path.removeSuffix(".json")

        val activators = getThings(folderPath, "activators", locationNode)
        val creatures = getThings(folderPath, "creatures", locationNode)
        val items = getThings(folderPath, "items", locationNode)
        val other = getThings(folderPath, "other", locationNode)
        return Location(locationNode, activators, creatures, items, other, properties.parsed())
    }
}
package core

import core.thing.ThingP
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import system.persistance.clean
import system.persistance.cleanPathToFile
import system.persistance.writeSave
import traveling.location.Network

fun persist(dataObject: Player, path: String) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile("json", prefix)
    val playerP = PlayerP(dataObject)
    val data = Json.encodeToString(playerP)

    writeSave(path, saveName, data)
    playerP.persistReferences(path)
}

@kotlinx.serialization.Serializable
data class PlayerP(
    val thing: ThingP,
) {
    constructor(b: Player) : this(ThingP(b.thing))

    fun parsed(playerName: String, path: String, parentLocation: Network? = null): Player {
        return Player(playerName, thing.parsed(path, parentLocation))
    }

    fun persistReferences(path: String) {
        val prefix = clean(path, thing.name)
        thing.persistReferences(prefix)
    }
}
package core

import core.thing.ThingP
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import system.mapper
import system.persistance.clean
import system.persistance.cleanPathToFile
import system.persistance.writeSave
import traveling.location.Network

suspend fun persist(dataObject: Player, path: String) {
    val prefix = clean(path, dataObject.name)
    val saveName = cleanPathToFile("json", prefix)
    val playerP = PlayerP(dataObject)
    val data = mapper.encodeToString(playerP)

    writeSave(path, saveName, data)
    playerP.persistReferences(path)
}

@kotlinx.serialization.Serializable
data class PlayerP(
    val thing: ThingP,
) {
    constructor(b: Player) : this(ThingP(b.thing))

    suspend fun parsed(playerName: String, path: String, parentLocation: Network? = null): Player {
        return Player(playerName, thing.parsed(path, parentLocation))
    }

    suspend fun persistReferences(path: String) {
        val prefix = clean(path, thing.name)
        thing.persistReferences(prefix)
    }
}

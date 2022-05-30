package system.persistance

import core.Player
import core.properties.Properties
import traveling.location.Network

actual fun getGameNames(): List<String> {
    throw NotImplementedError()
}

actual fun getCharacterSaves(gameName: String): List<String> {
    throw NotImplementedError()
}

actual inline fun <reified T> loadFromPath(path: String): T? {
    throw NotImplementedError()
}

actual fun save(rawGameName: String) {
    throw NotImplementedError()
}

actual fun save(gameName: String, network: Network) {
    throw NotImplementedError()
}

actual fun loadGame(gameName: String) {
    throw NotImplementedError()
}

actual fun loadCharacter(gameName: String, saveName: String, playerName: String): Player {
    throw NotImplementedError()
}

actual fun getGamesMetaData(): Properties {
    throw NotImplementedError()
}

actual fun writeSave(directoryName: String, saveName: String, json: String) {
    throw NotImplementedError()
}
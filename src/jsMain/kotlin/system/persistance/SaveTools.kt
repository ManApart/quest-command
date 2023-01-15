package system.persistance

import LocalForage.config
import LocalForageConfig
import core.*
import core.history.SessionHistory
import core.properties.Properties
import core.properties.PropertiesP
import getForage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import traveling.location.Network
import traveling.location.location.LocationManager

fun createDB() {
    config(LocalForageConfig("quest-command"))
}

actual class File actual constructor(pathIn: String) {
    actual val path = pathIn
    actual val nameWithoutExtension: String = pathIn.replace(".json", "")

    actual suspend fun readText(): String {
        return getForage(path)
    }

    suspend fun exists(): Boolean {
        return getForage(path) as String? != null
    }

}

actual suspend fun getGameNames(): List<String> {
    return getForage("game-names")
}

actual suspend fun getCharacterSaves(gameName: String): List<String> {
    return emptyList()
}

actual suspend inline fun <reified T> loadFromPath(path: String): T? {
    val file = File(path)
    return if (file.exists()) {
        Json.decodeFromString(file.readText())
    } else null
}

actual suspend fun save(rawGameName: String) {
    val gameName = cleanPathPart(rawGameName)
    val gamePath = clean(getSaveFolder(), gameName)
    saveSessionStats()
    GameState.players.values.forEach {
        persist(it, gamePath)
    }
    LocationManager.getNetworks().forEach { save(gamePath, it) }
    saveGameState(gamePath)
    saveTopLevelMetadata(gameName)
}

private suspend fun saveSessionStats() {
    if (!GameState.properties.values.getBoolean(SKIP_SAVE_STATS)) {
        SessionHistory.saveSessionStats()
    }
}

private suspend fun saveGameState(path: String) {
    GameState.properties.values.put(AUTO_LOAD, true)
    val gameStateSaveName = cleanPathToFile(".json", path, "gameState")
    val json = Json.encodeToString(GameStateP())
    writeSave(path, gameStateSaveName, json)
}

private suspend fun saveTopLevelMetadata(gameName: String) {
    val gameMetaData = Properties()
    gameMetaData.values.put(LAST_SAVE_GAME_NAME, gameName)
    gameMetaData.values.put(AUTO_LOAD, true)
    val json = Json.encodeToString(PropertiesP(gameMetaData))
    writeSave(getSaveFolder(), cleanPathToFile(".json", getSaveFolder(), "games"), json)
}

actual suspend fun save(gameName: String, network: Network) {
    throw NotImplementedError()
}

actual suspend fun loadGame(gameName: String) {
    throw NotImplementedError()
}

actual suspend fun loadCharacter(gameName: String, saveName: String, playerName: String): Player {
    val path = cleanPathToFile(".json", getSaveFolder(), gameName, saveName)
    val json: PlayerP = loadFromPath(path)!!
    return json.parsed(playerName, path, null)
}

actual suspend fun getGamesMetaData(): Properties {
    val props: PropertiesP? = loadFromPath(cleanPathToFile(".json", getSaveFolder(), "games"))
    return props?.parsed() ?: Properties()
}

actual suspend fun writeSave(directoryName: String, saveName: String, json: String) {
    throw NotImplementedError()
}

actual suspend fun getFiles(path: String, ignoredFileNames: List<String>): List<File> {
    throw NotImplementedError()
}
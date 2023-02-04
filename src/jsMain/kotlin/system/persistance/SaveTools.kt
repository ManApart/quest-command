package system.persistance

import LocalForage.config
import LocalForageConfig
import core.*
import core.commands.CommandParsers
import core.history.GameLogger
import core.history.SessionHistory
import core.history.TerminalPrinter
import core.properties.Properties
import core.properties.PropertiesP
import getForage
import getForageKeys
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import setForage
import traveling.location.Network
import traveling.location.location.LocationManager
import traveling.location.network.NOWHERE_NODE

fun createDB() {
    config(LocalForageConfig("quest-command"))
}

actual class File actual constructor(pathIn: String) {
    actual val path = pathIn
    actual val nameWithoutExtension: String = pathIn.substringAfterLast("/").replace(".json", "")
    val pathWithoutExtension: String = pathIn.replace(".json", "")

    actual suspend fun readText(): String {
        return getForage(path) ?: ""
    }

    suspend fun exists(): Boolean {
        return getForage(path) as String? != null
    }

}

actual suspend fun getGameNames(): List<String> {
    return getFolders(getSaveFolder()).map { it.nameWithoutExtension }
}

actual suspend fun getCharacterSaves(gameName: String): List<String> {
    return getFiles(clean(getSaveFolder(), gameName))
        .map { it.nameWithoutExtension }
        .filter { !ignoredGameSaveNames.contains(it) }
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
    network.getLocationNodes()
        .filter { it.hasLoadedLocation() }
        .map {
            val path = clean(gameName, network.name)
            traveling.location.location.persist(it.getLocation(), path)
            if (it !== NOWHERE_NODE) it.loadPath = cleanPathToFile(".json", path, it.name)
            it.flushLocation()
        }
}

actual suspend fun loadGame(gameName: String) {
    val gameStateData: GameStateP = loadFromPath(cleanPathToFile(".json", getSaveFolder(), gameName, "gameState"))!!
    gameStateData.updateGameState()
    GameLogger.stopTracking(GameState.player)
    println("Here 2")
    val newPlayers = gameStateData.characterNames.map { name ->
        loadCharacter(gameName, name, name)
    }
    println("Here 3")
    GameState.players.clear()
    newPlayers.forEach {
        GameState.putPlayer(it)
        it.location.getLocation().addThing(it.thing)
    }
    println("Here 4")
    GameState.player = newPlayers.first()
    GameLogger.reset()
    CommandParsers.reset()
    GameManager.playing = true
    println("Here 5")
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
    setForage(saveName, json)
}

actual suspend fun getFiles(path: String, ignoredFileNames: List<String>): List<File> {
    return getForageKeys().filter {
        it.startsWith(path) && it.endsWith(".json") && ignoredFileNames.none { ignored -> it.endsWith(ignored) }
    }.map { File(it) }
}

private suspend fun getFolders(path: String, ignoredFileNames: List<String> = listOf()): List<File> {
    return getForageKeys()
        .asSequence()
        .filter { it.startsWith(path) }
        .map { it.substring(0, it.indexOf("/", path.length + 1)) }
        .toSet()
        .filter { folder -> folder != path && folder.isNotBlank() && ignoredFileNames.none { folder.endsWith(it) } }
        .map { File(it) }
        .toList()
}
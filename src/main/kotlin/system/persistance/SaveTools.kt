package system.persistance

import core.*
import core.commands.CommandParsers
import core.history.GameLogger
import core.history.SessionHistory
import core.properties.Properties
import core.properties.PropertiesP
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import traveling.location.Network
import traveling.location.location.LocationManager
import traveling.location.network.NOWHERE_NODE
import java.io.File

private const val directory = "./saves/"
private val ignoredNames = listOf("games", "gameState")

fun getGameNames(): List<String> {
    return getFolders(directory).map { it.name }
}

fun getCharacterSaves(gameName: String): List<String> {
    return getFiles(clean(directory, gameName))
        .map { it.name }
        .filter { it.endsWith(".json") }
        .map { it.substring(0, it.length - ".json".length) }
        .filter { !ignoredNames.contains(it) }
}

inline fun <reified T> loadFromPath(path: String): T? {
    val file = File(path)
    return if (file.exists()) {
        Json.decodeFromString(file.readText())
    } else null
}

fun getFiles(path: String, ignoredFileNames: List<String> = listOf()): List<File> {
    return getFilesAndFolders(path, ignoredFileNames).filter { !it.isDirectory }
}

fun getFolders(path: String, ignoredFileNames: List<String> = listOf()): List<File> {
    return getFilesAndFolders(path, ignoredFileNames).filter { it.isDirectory }
}

private fun getFilesAndFolders(path: String, ignoredFileNames: List<String> = listOf()): List<File> {
    val folder = File(path)
    return if (folder.exists() && folder.isDirectory) {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        folder.listFiles().filter { file ->
            ignoredFileNames.none {
                file.endsWith(it)
            }
        }.toList()
    } else {
        listOf()
    }
}


fun save(rawGameName: String) {
    val gameName = cleanPathPart(rawGameName)
    val gamePath = clean(directory, gameName)
    saveSessionStats()
    GameState.players.values.forEach {
        persist(it, gamePath)
    }
    LocationManager.getNetworks().forEach { save(gamePath, it) }
    saveGameState(gamePath)
    saveTopLevelMetadata(gameName)
}

private fun saveSessionStats() {
    if (!GameState.properties.values.getBoolean(SKIP_SAVE_STATS)) {
        SessionHistory.saveSessionStats()
    }
}

private fun saveGameState(path: String) {
    GameState.properties.values.put(AUTO_LOAD, true)
    val gameStateSaveName = cleanPathToFile(".json", path, "gameState")
    val json = Json.encodeToString(GameStateP())
    writeSave(path, gameStateSaveName, json)
}

private fun saveTopLevelMetadata(gameName: String) {
    val gameMetaData = Properties()
    gameMetaData.values.put(LAST_SAVE_GAME_NAME, gameName)
    gameMetaData.values.put(AUTO_LOAD, true)
    val json = Json.encodeToString(PropertiesP(gameMetaData))
    writeSave(directory, cleanPathToFile(".json", directory, "games"), json)
}

fun save(gameName: String, network: Network) {
    network.getLocationNodes()
        .filter { it.hasLoadedLocation() }
        .map {
            val path = clean(gameName, network.name)
            traveling.location.location.persist(it.getLocation(), path)
            if (it !== NOWHERE_NODE) it.loadPath = cleanPathToFile(".json", path, it.name)
            it.flushLocation()
        }
}

//Instead of saving character at top level, save the path to the character's location and load that?
fun loadGame(gameName: String) {
    loadGameState(gameName)
    GameLogger.stopTracking(GameState.player)
    GameState.players.values.forEach { player ->
        loadCharacter(gameName, player.thing.name, player.name)
    }
    GameLogger.track(GameState.player)
    CommandParsers.addParser(GameState.player)
    GameManager.playing = true
}

fun loadGameState(gameName: String) {
    val gameStateData: GameStateP = loadFromPath(cleanPathToFile(".json", directory, gameName, "gameState"))!!
    gameStateData.updateGameState()
}

fun loadCharacter(gameName: String, saveName: String, playerName: String) {
    val path = cleanPathToFile(".json", directory, gameName, saveName)
    val json: PlayerP = loadFromPath(path)!!
    GameState.putPlayer(json.parsed(playerName, path, null))
}

fun getGamesMetaData(): Properties {
    val props: PropertiesP? = loadFromPath(cleanPathToFile(".json", directory, "games"))
    return props?.parsed() ?: Properties()
}

fun writeSave(directoryName: String, saveName: String, json: String) {
    val directory = File(directoryName)
    if (!directory.exists()) {
        directory.mkdirs()
    }
    File(saveName).printWriter().use { out ->
        out.println(json)
    }
}




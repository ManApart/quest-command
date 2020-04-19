package system.persistance

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.*
import core.history.SessionHistory
import core.properties.Properties
import core.target.Target
import core.target.persist
import core.target.load
import traveling.location.Network
import traveling.location.location.LocationManager
import java.io.File

private const val directory = "./saves/"
private val ignoredNames = listOf("games", "gameState")

private val mapper = jacksonObjectMapper()

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

fun loadMaps(path: String): List<Map<String, Any>> {
    return getFiles(path).map { loadMap(it.path) }
}

fun loadMap(path: String): Map<String, Any> {
    val stream = File(path)
    return if (stream.exists()) {
        mapper.readValue(stream)
    } else {
        mapOf()
    }
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


fun save(rawGameName: String, player: Target) {
    val gameName = cleanPathPart(rawGameName)
    val gamePath = clean(directory, gameName)
    saveSessionStats()
    persist(player, gamePath)
    LocationManager.getNetworks().forEach { save(gamePath, it) }
    saveGameState(player, gamePath)
    saveTopLevelMetadata(gameName)
}

private fun saveSessionStats() {
    if (!GameState.properties.values.getBoolean(SKIP_SAVE_STATS)) {
        SessionHistory.saveSessionStats()
    }
}

private fun saveGameState(player: Target, path: String) {
    GameState.properties.values.put(LAST_SAVE_CHARACTER_NAME, cleanPathPart(player.name))
    GameState.properties.values.put(AUTO_LOAD, true)
    val gameData = getPersistedGameState()
    val gameStateSaveName = cleanPathToFile(".json", path, "gameState")
    writeSave(path, gameStateSaveName, gameData)
}

private fun saveTopLevelMetadata(gameName: String) {
    val gameMetaData = Properties()
    gameMetaData.values.put(LAST_SAVE_GAME_NAME, gameName)
    gameMetaData.values.put(AUTO_LOAD, true)
    val gameMetaDataData = core.properties.getPersisted(gameMetaData)
    writeSave(directory, cleanPathToFile(".json", directory, "games"), gameMetaDataData)
}

fun save(gameName: String, network: Network) {
    network.getLocationNodes()
            .filter { it.hasLoadedLocation() }
            .map {
                val path = clean(gameName, network.name)
                traveling.location.location.persist(it.getLocation(), path)
                it.loadPath = cleanPathToFile(".json", path, it.name)
                it.flushLocation()
            }
}

//Instead of saving character at top level, save the path to the character's location and load that?
fun loadGame(gameName: String) {
    loadGameState(gameName)
    val characterName = GameState.properties.values.getString(LAST_SAVE_CHARACTER_NAME, getCharacterSaves(gameName).first())
    loadCharacter(gameName, characterName)
    GameManager.playing = true
}

fun loadGameState(gameName: String) {
    val gameStateData = loadMap(cleanPathToFile(".json", directory, gameName, "gameState"))
    readGameStateFromData(gameStateData)
}

fun loadCharacter(gameName: String, saveName: String) {
//    ScopeManager.getScope().removeTarget(GameState.player)
    GameState.player = load(cleanPathToFile(".json", directory, gameName, saveName))
//    ScopeManager.getScope(GameState.player.location).addTarget(GameState.player)
}

fun getGamesMetaData(): Properties {
    val data = loadMap(cleanPathToFile(".json", directory, "games"))
    return core.properties.readFromData(data)
}

fun writeSave(directoryName: String, saveName: String, data: Map<String, Any>) {
    val directory = File(directoryName)
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val json = ObjectMapper().writeValueAsString(data)
    File(saveName).printWriter().use { out ->
        out.println(json)
    }
}




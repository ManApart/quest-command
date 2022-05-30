package system.persistance

import core.*
import core.commands.CommandParsers
import core.history.GameLogger
import core.history.SessionHistory
import core.history.TerminalPrinter
import core.properties.Properties
import core.properties.PropertiesP
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import traveling.location.Network
import traveling.location.location.LocationManager
import traveling.location.network.NOWHERE_NODE
import java.io.File

private val ignoredNames = listOf("games", "gameState")

private fun getSaveFolder(): String {
    return if (GameState.properties.values.getBoolean(TEST_SAVE_FOLDER)) "./savesTest/" else "./saves/"
}

actual fun getGameNames(): List<String> {
    return getFolders(getSaveFolder()).map { it.name }
}

actual fun getCharacterSaves(gameName: String): List<String> {
    return getFiles(clean(getSaveFolder(), gameName))
        .map { it.name }
        .filter { it.endsWith(".json") }
        .map { it.substring(0, it.length - ".json".length) }
        .filter { !ignoredNames.contains(it) }
}

actual inline fun <reified T> loadFromPath(path: String): T? {
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


actual fun save(rawGameName: String) {
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
    writeSave(getSaveFolder(), cleanPathToFile(".json", getSaveFolder(), "games"), json)
}

actual fun save(gameName: String, network: Network) {
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
actual fun loadGame(gameName: String) {
    val gameStateData: GameStateP = loadFromPath(cleanPathToFile(".json", getSaveFolder(), gameName, "gameState"))!!
    gameStateData.updateGameState()
    GameLogger.stopTracking(GameState.player)
    val newPlayers = gameStateData.characterNames.map { name ->
        loadCharacter(gameName, name, name)
    }
    GameState.players.clear()
    newPlayers.forEach {
        GameState.putPlayer(it)
        it.location.getLocation().addThing(it.thing)
    }
    GameState.player = newPlayers.first()
    GameLogger.reset()
    CommandParsers.reset()
    TerminalPrinter.reset()
    GameManager.playing = true
}


actual fun loadCharacter(gameName: String, saveName: String, playerName: String): Player {
    val path = cleanPathToFile(".json", getSaveFolder(), gameName, saveName)
    val json: PlayerP = loadFromPath(path)!!
    return json.parsed(playerName, path, null)
}

actual fun getGamesMetaData(): Properties {
    val props: PropertiesP? = loadFromPath(cleanPathToFile(".json", getSaveFolder(), "games"))
    return props?.parsed() ?: Properties()
}

actual fun writeSave(directoryName: String, saveName: String, json: String) {
    val directory = File(directoryName)
    if (!directory.exists()) {
        directory.mkdirs()
    }
    File(saveName).printWriter().use { out ->
        out.println(json)
    }
}




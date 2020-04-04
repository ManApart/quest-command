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
import traveling.location.location.Location
import traveling.location.location.LocationManager
import traveling.location.location.LocationNode
import java.io.File

private const val directory = "./saves/"
private val ignoredNames = listOf("games", "gameState")

private val mapper = jacksonObjectMapper()

fun clean(pathString: String): String {
    return pathString.replace(" ", "_").replace(Regex("[^a-zA-Z]"), "")
}

fun getGameNames(): List<String> {
    return getFiles(directory).map { it.name }.filter { !it.endsWith(".json") }
}

fun getCharacterSaves(gameName: String): List<String> {
    return getFiles(directory + "/" + clean(gameName) + "/")
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

fun getFiles(path: String): List<File> {
    val folder = File(path)
    return if (folder.exists() && folder.isDirectory) {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        folder.listFiles().toList()
    } else {
        listOf()
    }
}

fun save(rawGameName: String, player: Target) {
    val gameName = clean(rawGameName)
    val gamePath = "$directory${gameName}/"
    saveSessionStats()
    savePlayer(player, gamePath)
//    LocationManager.getNetworks().flatMap { it.getLocationNodes() }.forEach { it.flushLocation() }
    LocationManager.getNetworks().forEach { save(gamePath, it) }
    saveGameState(player, gamePath)
    saveTopLevelMetadata(gameName)
}

private fun saveSessionStats() {
    if (!GameState.properties.values.getBoolean(SKIP_SAVE_STATS)) {
        SessionHistory.saveSessionStats()
    }
}

private fun savePlayer(player: Target, path: String) {
    val playerData = persist(player, path)

}

private fun saveGameState(player: Target, path: String) {
    GameState.properties.values.put(LAST_SAVE_CHARACTER_NAME, clean(player.name))
    GameState.properties.values.put(AUTO_LOAD, true)
    val gameData = getPersistedGameState()
    val gameStateSaveName = path + "gameState.json"
    writeSave(path, gameStateSaveName, gameData)
}

private fun saveTopLevelMetadata(gameName: String) {
    val gameMetaData = Properties()
    gameMetaData.values.put(LAST_SAVE_GAME_NAME, gameName)
    gameMetaData.values.put(AUTO_LOAD, true)
    val gameMetaDataData = core.properties.getPersisted(gameMetaData)
    writeSave(directory, directory + "games.json", gameMetaDataData)
}

fun save(gameName: String, network: Network) {
    network.getLocationNodes()
            .filter { it.hasLoadedLocation() }
            .map {
                val path = "$gameName/${clean(network.name)}/"
                traveling.location.location.persist(it.getLocation(), path)
                it.loadPath = "$path/${it.name}.json"
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
    val gameStateData = loadMap("$directory/${gameName}/gameState.json")
    readGameStateFromData(gameStateData)
}

fun loadCharacter(gameName: String, saveName: String) {
//    ScopeManager.getScope().removeTarget(GameState.player)
    GameState.player = load("$directory/${gameName}/$saveName.json")
//    ScopeManager.getScope(GameState.player.location).addTarget(GameState.player)
}

fun getGamesMetaData(): Properties {
    val data = loadMap(directory + "games.json")
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


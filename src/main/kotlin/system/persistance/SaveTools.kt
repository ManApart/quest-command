package system.persistance

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.*
import core.history.SessionHistory
import core.properties.Properties
import core.target.Target
import core.target.getPersisted
import core.target.readFromData
import traveling.location.location.LocationNode
import traveling.scope.Scope
import traveling.scope.ScopeManager
import traveling.scope.getPersisted
import java.io.File

private const val directory = "./saves/"
private val ignoredNames = listOf("games", "gameState")

fun clean(pathString: String): String {
    return pathString.replace(" ", "_").replace(Regex("[^a-zA-Z]"), "")
}

fun getGameNames(): List<String> {
    return getFiles(directory).map { it.name }.filter { !it.endsWith(".json") }
}

fun getCharacterSaves(gameName: String): List<String> {
    return getFiles(directory + "/" + clean(gameName) + "/")
            .map{it.name}
            .filter { it.endsWith(".json") }
            .map { it.substring(0, it.length - ".json".length) }
            .filter { !ignoredNames.contains(it) }
}

private fun getFiles(path: String): List<File> {
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
    ScopeManager.flush()
    saveGameState(player, gamePath)
    saveTopLevelMetadata(gameName)
}

private fun saveSessionStats() {
    if (!GameState.properties.values.getBoolean(SKIP_SAVE_STATS)) {
        SessionHistory.saveSessionStats()
    }
}

private fun savePlayer(player: Target, path: String) {
    val playerData = getPersisted(player)
    val saveName = path + clean(player.name) + ".json"
    writeSave(path, saveName, playerData)
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

fun save(gameName: String, scope: Scope) {
    val path = "$directory${clean(gameName)}/${clean(scope.locationNode.network.name)}/"
    val data = getPersisted(scope)
    val saveName = "$path${clean(scope.locationNode.name)}.json"
    writeSave(path, saveName, data)
}

fun loadGame(gameName: String) {
    loadGameState(gameName)
    val characterName = GameState.properties.values.getString(LAST_SAVE_CHARACTER_NAME, getCharacterSaves(gameName).first())
    loadCharacter(gameName, characterName)
    GameManager.playing = true
}

fun loadGameState(gameName: String) {
    val gameStateData = readSave("$directory/${gameName}/gameState.json")
    readGameStateFromData(gameStateData)
}

fun loadCharacter(gameName: String, saveName: String) {
    val playerData = readSave("$directory/${gameName}/$saveName.json")
    ScopeManager.getScope().removeTarget(GameState.player)
    GameState.player = readFromData(playerData)
    ScopeManager.getScope(GameState.player.location).addTarget(GameState.player)
}

fun loadScope(gameName: String, locationNode: LocationNode): Scope {
    val path = "$directory${clean(gameName)}/${clean(locationNode.network.name)}/${clean(locationNode.name)}.json"
    val data = readSave(path)
    return traveling.scope.readFromData(data, locationNode)
}

fun getGamesMetaData(): Properties {
    val data = readSave(directory + "games.json")
    return core.properties.readFromData(data)
}

private fun writeSave(directoryName: String, saveName: String, data: Map<String, Any>) {
    val directory = File(directoryName)
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val json = ObjectMapper().writeValueAsString(data)
    File(saveName).printWriter().use { out ->
        out.println(json)
    }
}

private fun readSave(savePath: String): Map<String, Any> {
    val stream = File(savePath)
    return if (stream.exists()) {
        jacksonObjectMapper().readValue(stream)
    } else {
        mapOf()
    }
}
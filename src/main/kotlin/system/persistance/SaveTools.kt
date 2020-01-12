package system.persistance

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.GameState
import core.LAST_SAVE_CHARACTER_NAME
import core.SKIP_SAVE_STATS
import core.history.SessionHistory
import core.readGameStateFromData
import core.target.Target
import core.target.getPersisted
import core.target.readFromData
import traveling.location.location.LocationNode
import traveling.scope.Scope
import traveling.scope.ScopeManager
import traveling.scope.getPersisted
import java.io.File

private const val directory = "./saves/"

fun clean(pathString: String): String {
    return pathString.replace(" ", "_").replace(Regex("[^a-zA-Z]"), "")
}

//TODO - handle no files
fun getGameNames(): List<String> {
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return File(directory).listFiles().map { it.name }
}

//TODO - handle no files
fun getCharacterSaves(gameName: String): List<String> {
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    return File(directory + "/" + clean(gameName) + "/").listFiles().map { it.name.substring(0, it.name.length - ".json".length) }
}

// Saving always saves both the game and the character
fun save(gameName: String, player: Target) {
    if (!GameState.properties.values.getBoolean(SKIP_SAVE_STATS)) {
        SessionHistory.saveSessionStats()
    }
    val playerData = getPersisted(player)
    val path = "$directory${clean(gameName)}/"
    val saveName = path + clean(player.name) + ".json"
    writeSave(path, saveName, playerData)

    ScopeManager.flush()

    GameState.properties.values.put(LAST_SAVE_CHARACTER_NAME, clean(player.name))

    val gameData = core.getPersistedGameState()
    val gameStateSaveName = path + "gameState.json"
    writeSave(path, gameStateSaveName, gameData)
}

fun save(gameName: String, scope: Scope) {
    val path = "$directory${clean(gameName)}/${clean(scope.locationNode.network.name)}/"
    val data = getPersisted(scope)
    val saveName = "$path/${clean(scope.locationNode.name)}.json"
    writeSave(path, saveName, data)
}

fun loadGame(gameName: String) {
    loadGameState(gameName)
    val characterName = GameState.properties.values.getString(LAST_SAVE_CHARACTER_NAME, getCharacterSaves(gameName).first())
    loadCharacter(gameName, characterName)
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
    val path = "$directory${clean(gameName)}/${clean(locationNode.network.name)}/${clean(locationNode.name)}"
    val data = readSave(path)
    return traveling.scope.readFromData(data, locationNode)
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
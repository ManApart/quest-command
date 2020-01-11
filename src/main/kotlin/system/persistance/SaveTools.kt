package system.persistance

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.GameState
import core.SKIP_SAVE_STATS
import core.history.SessionHistory
import core.target.Target
import core.target.getPersisted
import core.target.readFromData
import traveling.scope.ScopeManager
import java.io.File

private const val directory = "./saves/"

fun clean(pathString: String): String {
    return pathString.replace(" ", "_").replace(Regex("[^a-zA-Z]"), "")
}

fun getGameNames(): List<String> {
    return File(directory).listFiles().map { it.name }
}

fun getCharacterSaves(gameName: String): List<String> {
    return File(directory + "/" + clean(gameName) + "/").listFiles().map { it.name.substring(0, it.name.length - ".json".length) }
}

// Saving always saves both the game and the character
fun save(gameName: String, player: Target) {
    if (!GameState.properties.values.getBoolean(SKIP_SAVE_STATS)) {
        SessionHistory.saveSessionStats()
    }
    val playerData = getPersisted(player)
    val saveName = generateSaveName(gameName, player.name)
    writeSave(clean(gameName), saveName, playerData)
}

fun loadGame(gameName: String) {

    loadCharacter(gameName, getCharacterSaves(gameName).first())
}

fun loadCharacter(gameName: String, saveName: String) {
    val playerData = readSave("$directory/${gameName}/$saveName.json")

    ScopeManager.getScope().removeTarget(GameState.player)
    GameState.player = readFromData(playerData)
    ScopeManager.getScope().addTarget(GameState.player)
}

private fun generateSaveName(gameName: String, playerName: String): String {
    return directory + clean(gameName) + "/" + clean(playerName) + ".json"
}

private fun writeSave(gameName: String, saveName: String, data: Map<String, Any>) {
    val directory = File("$directory$gameName/")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val json = ObjectMapper().writeValueAsString(data)
    File(saveName).printWriter().use { out ->
        out.println(json)
    }
}

private fun readSave(playerSavePath: String): Map<String, Any> {
    val stream = File(playerSavePath)
    return jacksonObjectMapper().readValue(stream)
}
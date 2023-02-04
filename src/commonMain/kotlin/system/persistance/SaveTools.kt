package system.persistance

import core.*
import core.properties.Properties
import traveling.location.Network

expect suspend fun getGameNames(): List<String>
expect suspend fun getCharacterSaves(gameName: String): List<String>
expect suspend inline fun <reified T> loadFromPath(path: String): T?
expect suspend fun getFiles(path: String, ignoredFileNames: List<String> = listOf()): List<File>
expect suspend fun save(rawGameName: String)
expect suspend fun save(gameName: String, network: Network)
expect suspend fun loadGame(gameName: String)
expect suspend fun loadCharacter(gameName: String, saveName: String, playerName: String): Player
expect suspend fun getGamesMetaData(): Properties
expect suspend fun writeSave(directoryName: String, saveName: String, json: String)


expect class File(pathIn: String) {
    val path: String
    val nameWithoutExtension: String
    suspend fun readText(): String
}

val ignoredGameSaveNames = listOf("games", "gameState")
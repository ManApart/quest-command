package system.persistance

import core.*
import core.properties.Properties
import io.ktor.http.ContentDisposition.Companion.File
import traveling.location.Network

expect fun getGameNames(): List<String>
expect fun getCharacterSaves(gameName: String): List<String>
expect inline fun <reified T> loadFromPath(path: String): T?
expect fun getFiles(path: String, ignoredFileNames: List<String> = listOf()): List<File>
expect fun getFolders(path: String, ignoredFileNames: List<String> = listOf()): List<File>
expect fun save(rawGameName: String)
expect fun save(gameName: String, network: Network)
expect fun loadGame(gameName: String)
expect fun loadCharacter(gameName: String, saveName: String, playerName: String): Player
expect fun getGamesMetaData(): Properties
expect fun writeSave(directoryName: String, saveName: String, json: String)



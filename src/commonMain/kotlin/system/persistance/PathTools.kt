package system.persistance

import core.GameState
import core.GameStateKeys.TEST_SAVE_FOLDER

val ignoredGameSaveNames = listOf("games", "gameState")

fun cleanPathToFile(extension: String, vararg pieces: String): String {
    val path = cleanPath(pieces.toList()).removeSuffix("/")
    return clean("$path.$extension").removeSuffix("/")
}

fun clean(vararg pieces: String): String {
    return cleanPath(pieces.toList())
}

private fun cleanPath(pieces: List<String>) : String {
    return (pieces.joinToString("/") { cleanPathPart(it) } + "/").replace("//", "/").replace("..", ".")
}

fun cleanPathPart(pathString: String): String {
    return pathString.replace(" ", "_").replace("\\", "/").replace(Regex("[^a-zA-Z/.]"), "")
}

fun getSaveFolder(): String {
    return if (GameState.properties.values.getBoolean(TEST_SAVE_FOLDER)) "./savesTest/" else "./saves/"
}
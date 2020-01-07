package system.persistance

import java.io.File

const val directory = "./saves/"

fun clean(pathString: String): String {
    return pathString.replace(" ", "_").replace(Regex("[^a-zA-Z]"), "")
}

fun getGameNames(): List<String> {
    return File(directory).listFiles().map { it.name }
}

fun getSaveNames(gameName: String): List<String> {
    return File(directory + "/" + clean(gameName) + "/").listFiles().map { it.name.substring(0, it.name.length - ".json".length) }
}
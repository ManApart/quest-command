package core.utility

import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object JsonDirectoryParser {

    fun <E> parseDirectory(directoryPath: String, parser: (path: String) -> List<E>): List<E> {
        return getResourceFiles(directoryPath).map {
            parser("$directoryPath/$it")
        }.flatten()
    }

    private fun getResourceFiles(path: String): List<String> {
        val uri = this::class.java.getResource(path).toURI()
        return if (uri.scheme == "jar") {
            getResourceFilesJAR(path, uri)
        } else {
            getResourceFilesIDE(uri)
        }
    }

    private fun getResourceFilesIDE(uri: URI): List<String> {
        return getResourceList(Paths.get(uri))
    }

    private fun getResourceFilesJAR(path: String, uri: URI): List<String> {
        val fileSystem = FileSystems.newFileSystem(uri, mutableMapOf<String, Any>())
        val resources = getResourceList(fileSystem.getPath(path))
        fileSystem?.close()
        return resources
    }

    private fun getResourceList(start: Path): List<String> {
        return Files.walk(start, 1).iterator().asSequence()
                .map {
                    it.getName(it.nameCount - 1).toString()
                }
                .filter { it.endsWith(".json") }
                .toList()
    }

}
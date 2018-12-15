package core.utility

import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

//TODO - make class with instance variables?
object ResourceHelper {
    fun getResourceFiles(path: String, recursive: Boolean = false): List<String> {

        val uri = this::class.java.getResource(path).toURI()
        return if (uri.scheme == "jar") {
            getResourceFilesJAR(path, uri, path, recursive)
        } else {
            getResourceFilesIDE(uri, path, recursive)
        }
    }

    private fun getResourceFilesIDE(uri: URI, root: String, recursive: Boolean): List<String> {
        return getResourceList(Paths.get(uri), root, recursive)
    }

    private fun getResourceFilesJAR(path: String, uri: URI, root: String, recursive: Boolean): List<String> {
        val fileSystem = FileSystems.newFileSystem(uri, mutableMapOf<String, Any>())
        val resources = getResourceList(fileSystem.getPath(path), root, recursive)
        fileSystem?.close()
        return resources
    }

    private fun getResourceList(start: Path, root: String, recursive: Boolean): List<String> {
        val maxDepth = if (recursive) Int.MAX_VALUE else 1
        return Files.walk(start, maxDepth).iterator().asSequence()
                .map {
                    getRelativeName(it, root)
                }
                .filter { it.endsWith(".json") }
                .toList()
    }

    private fun getRelativeName(path: Path, root: String) : String {
        val fullPath = path.toAbsolutePath().toString().replace("\\", "/")
        return fullPath.substring(fullPath.indexOf(root))
    }
}
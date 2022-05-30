package core.utility

interface ResourceHelper {
    fun getResourceFiles(path: String, recursive: Boolean = true): List<String>
}
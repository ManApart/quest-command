package core.utility

expect class KotlinResourceHelper() : ResourceHelper {
    override fun getResourceFiles(path: String, recursive: Boolean): List<String>
}
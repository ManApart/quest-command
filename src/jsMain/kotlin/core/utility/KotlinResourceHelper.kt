package core.utility


actual class KotlinResourceHelper : ResourceHelper {
    actual override fun getResourceFiles(path: String, recursive: Boolean): List<String> {
      throw NotImplementedError()
    }
}
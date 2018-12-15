package building.json

import core.utility.ResourceHelper

object JsonGenerator {
    fun generate(root: String) {
        getSourceFiles(root).forEach {
            JsonConverter(it, outputPath(it, root)).convert()
        }
    }

    private fun getSourceFiles(root: String): List<String> {
        return ResourceHelper.getResourceFiles("$root/src/content", true)
    }

    private fun outputPath(input: String, root: String) : String {
        return input.replace("$root/src", "$root/generated")
    }

}
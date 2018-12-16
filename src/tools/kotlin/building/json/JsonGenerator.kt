package building.json

import core.utility.ResourceHelper

object JsonGenerator {
    fun generate(writeRoot: String, inputPath: String, outputPath: String) {
        ResourceHelper.getResourceFiles(inputPath, true)
                .forEach {
                    JsonFileConverter(writeRoot + it, writeRoot + it.replace(inputPath, outputPath)).convert()
                }
    }


}
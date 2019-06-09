package building.json

import core.utility.KotlinResourceHelper

object JsonGenerator {
    fun generate(writeRoot: String, inputPath: String, outputPath: String) {

        KotlinResourceHelper().getResourceFiles(inputPath, true)
                .forEach {
                    JsonFileConverter(writeRoot + it, writeRoot + it.replace(inputPath, outputPath)).convert()
                }
    }


}
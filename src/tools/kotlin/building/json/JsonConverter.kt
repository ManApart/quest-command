package building.json

import java.io.File


class JsonConverter(val inputPath: String, val outputPath: String) {

    fun convert() {
        println("Convert '$inputPath' to '$outputPath'")

        val input = File(inputPath).readText()

        val output = File(outputPath)
        output.parentFile.mkdirs()

        output.printWriter().use { out ->
            out.println(input)
        }
    }
}
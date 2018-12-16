package building.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File


class JsonFileConverter(private val inputPath: String, private val outputPath: String) {

    fun convert() {
        println("Convert '$inputPath' to '$outputPath'")
        val mapper = jacksonObjectMapper()

        val input: List<MutableMap<String, Any>> = mapper.readValue(File(inputPath).readText())

        val transformed = JsonConverter(input).transform()

        val output = File(outputPath)
        output.parentFile.mkdirs()

        mapper.writeValue(output, transformed)
    }
}
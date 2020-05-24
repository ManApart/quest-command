package building.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.KotlinResourceHelper
import core.utility.putList
import java.io.File

object JsonGenerator {
    private val mapper = jacksonObjectMapper()

    fun generate(writeRoot: String, inputPath: String, outputPath: String) {
        val allConversions = KotlinResourceHelper().getResourceFiles(inputPath, true).map {
            createConversion(it, writeRoot, inputPath, outputPath)
        }

        val scopeMap = buildFolderMap(allConversions)
        val bodyMaker = BodyMaker(mapper)
        scopeMap.entries.forEach { (_, conversions) ->
            val converter = JsonConverter(conversions)
            conversions.forEach {
                val transformed = converter.transform(it)
                bodyMaker.add(transformed)
                val output = File(it.outputPath)
                output.parentFile.mkdirs()
                mapper.writeValue(output, transformed)
            }
        }
        bodyMaker.writeBodies()
        println("Generated ${allConversions.size} json files from ${scopeMap.keys.size} folders.")
    }

    private fun createConversion(path: String, writeRoot: String, inputPath: String, outputPath: String): JsonFileConversion {
        val adjustedInputPath = writeRoot + path
        val data: List<MutableMap<String, Any>> = mapper.readValue(File(adjustedInputPath).readText())
        return JsonFileConversion(adjustedInputPath, writeRoot + path.replace(inputPath, outputPath), data)
    }

    private fun buildFolderMap(conversions: List<JsonFileConversion>): Map<String, List<JsonFileConversion>> {
        val map = mutableMapOf<String, MutableList<JsonFileConversion>>()
        conversions.forEach {
            val scope = getScope(it.inputPath)
            map.putList(scope, it)
        }
        return map
    }

    private fun getScope(inputPath: String) : String {
        val pathChunks = inputPath.split("/")
        return when {
            pathChunks[pathChunks.size-2] == "story-events" -> pathChunks[pathChunks.size-1]
            pathChunks[7] == "items"-> pathChunks[7]
            else -> pathChunks[pathChunks.size-2]
        }
    }


}
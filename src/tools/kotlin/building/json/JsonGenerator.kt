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

        val folderMap = buildFolderMap(allConversions)
        folderMap.entries.forEach { (folder, conversions) ->
            val converter = JsonConverter2(conversions)
            conversions.forEach {
                val transformed = converter.transform(it)
                val output = File(it.outputPath)
                output.parentFile.mkdirs()
                mapper.writeValue(output, transformed)
            }
        }
        println("Generated ${allConversions.size} json files from ${folderMap.keys.size} folders.")
    }

    private fun createConversion(path: String, writeRoot: String, inputPath: String, outputPath: String): JsonFileConversion {
        val adjustedInputPath = writeRoot + path
        val data: List<MutableMap<String, Any>> = mapper.readValue(File(adjustedInputPath).readText())
        return JsonFileConversion(adjustedInputPath, writeRoot + path.replace(inputPath, outputPath), data)
    }

    private fun buildFolderMap(conversions: List<JsonFileConversion>): Map<String, List<JsonFileConversion>> {
        val map = mutableMapOf<String, MutableList<JsonFileConversion>>()
        conversions.forEach {
            val pathChunks = it.inputPath.split("/")
            var folder = pathChunks[pathChunks.size-2]
            //This is ugly, but forces the the scope of quest to the file level. This way different quests can have steps with the same name without issue
            if (folder == "story-events"){
               folder = pathChunks[pathChunks.size-1]
            }
            map.putList(folder, it)
        }
        return map
    }

}
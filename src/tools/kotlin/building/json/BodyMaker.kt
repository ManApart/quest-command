package building.json

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

class BodyMaker(private val mapper: ObjectMapper) {
    private val bodies = mutableListOf<Map<String, Any>>()
    private val bodyParts = mutableListOf<Map<String, Any>>()
    fun add(transformed: List<Map<String, Any>>) {
        transformed
                .mapNotNull { it["body"] as Map<String, Any>? }
                .forEach {
                    if (it["bodyPart"] != null) {
                        bodyParts.add(it["bodyPart"] as Map<String, Any>)
                    }
//                    if (it["body"] == null && it["bodyName"] == null) {
//                        //add a body with the name
//                    }
                    val body = createBody(it)
                    bodies.add(body)

                }
    }

    private fun createBody(source: Map<String, Any>): Map<String, Any> {
        return source.toMutableMap().apply {
            remove("bodyPart")
            if (this["parent"] == null){
                this["parent"] = this["name"] as String
            }
        }
    }

    fun writeBodies() {
        val bodiesOutput = File("./src/main/resource/data/generated/content/bodies/bodies/Generated.json")
        bodiesOutput.parentFile.mkdirs()
        mapper.writeValue(bodiesOutput, bodies)

        val bodyOutput = File("./src/main/resource/data/generated/content/bodies/parts/Generated.json")
        bodyOutput.parentFile.mkdirs()
        mapper.writeValue(bodyOutput, bodyParts)
    }
}
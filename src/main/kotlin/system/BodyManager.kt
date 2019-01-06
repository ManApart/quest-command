package system

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.gameState.bodies.Body
import core.gameState.bodies.BodyPart
import core.gameState.bodies.ProtoBody
import core.utility.JsonDirectoryParser

object BodyManager {
    private val protoBodies = JsonDirectoryParser.parseDirectory("/data/generated/content/bodies/bodies", ::parseBodiesFile)
    private fun parseBodiesFile(path: String): List<ProtoBody> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    private val bodyParts = JsonDirectoryParser.parseDirectory("/data/generated/content/bodies/parts", ::parsePartsFile)
    private fun parsePartsFile(path: String): List<BodyPart> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    private val bodies = createBodies(protoBodies, bodyParts)

    private fun createBodies(protoBodies: List<ProtoBody>, bodyParts: List<BodyPart>): List<Body> {
        val partMap = bodyParts.map { it.name.toLowerCase() to it }.toMap()
        val bodies = mutableListOf<Body>()

        protoBodies.forEach {
            val parts = findAllParts(it, partMap)
            bodies.add(Body(it.name, parts))
        }
        return bodies
    }

    private fun findAllParts(it: ProtoBody, partMap: Map<String, BodyPart>): List<BodyPart> {
        val parts = mutableListOf<BodyPart>()
        it.parts.forEach { partName ->
            if (!partMap.containsKey(partName.toLowerCase())) {
                throw IllegalArgumentException("Part $partName does not exist as a body part!")
            } else {
                parts.add(partMap[partName.toLowerCase()]!!)
            }
        }
        return parts
    }

    fun bodyExists(name: String): Boolean {
        return bodies.firstOrNull { it.name.toLowerCase() == name.toLowerCase() } != null
    }

    fun getBody(name: String): Body {
        return Body(bodies.first { it.name.toLowerCase() == name.toLowerCase() })
    }
}
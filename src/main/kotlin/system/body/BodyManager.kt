package system.body

import core.gameState.body.Body
import core.gameState.body.BodyPart
import core.gameState.body.ProtoBody
import system.DependencyInjector

object BodyManager {
    private var parser = DependencyInjector.getImplementation(BodyParser::class.java)
    private var bodies = createBodies()

    fun reset() {
        parser = DependencyInjector.getImplementation(BodyParser::class.java)
        bodies = createBodies()
    }

    private fun createBodies(): List<Body> {
        val protoBodies = parser.loadBodies()
        val bodyParts = parser.loadBodyParts()

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
package core.body

import traveling.location.location.LocationNode

interface BodyParser {
    fun loadBodyParts(): List<BodyPart>
    fun loadBodies(): List<LocationNode>
}
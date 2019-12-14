package core.body

import traveling.location.LocationNode

interface BodyParser {
    fun loadBodyParts(): List<BodyPart>
    fun loadBodies(): List<LocationNode>
}
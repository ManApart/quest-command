package explore.look

import core.Player
import core.body.NONE
import core.history.displayToMe
import core.history.displayToOthers
import core.thing.Thing
import crafting.material.DEFAULT_MATERIAL
import traveling.position.NO_VECTOR

suspend fun describeBody(source: Player, thing: Thing) {
    val body = thing.body
    if (body.name == NONE.name) {
        val materialString = if (body.core.material.name != DEFAULT_MATERIAL.name) " It is made of ${body.core.material.name}." else ""
        source.displayToMe("This has no body.$materialString")
    } else {
        val parts = body.parts.joinToString(", ") { it.name }
        source.displayToMe("${body.name} body has parts: $parts")
    }
    source.displayToOthers("${source.name} looks at ${thing.name}'s body.")
}

suspend fun describeBodyDetailed(source: Player, thing: Thing) {
    describeBody(source, thing)
    if (thing.getDimensions() != NO_VECTOR) {
        source.displayToMe("${thing.body.name} body has dimensions: ${thing.getDimensions()}")
    }
}

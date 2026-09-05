package explore.look

import core.Player
import core.body.BodyPart
import core.body.NONE
import core.body.body
import core.history.displayToMe
import core.history.displayToOthers
import core.thing.Thing
import core.utility.asSubject
import core.utility.ifYouWord
import crafting.material.DEFAULT_MATERIAL
import traveling.position.NO_VECTOR

suspend fun describeBody(source: Player, thing: Thing) {
    val body = thing.body
    if (body.name == NONE.name) source.displayToMe("This has no body.")
    if (body.parts.size == 1) {
        if (body.baseMaterial != DEFAULT_MATERIAL) source.displayToMe("It is made of ${body.core.material.name}.")
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

suspend fun describeBodyPart(source: Player, target: Thing, part: BodyPart) {
    if (part.material != DEFAULT_MATERIAL) source.displayToMe("${part.name} is made of ${part.material.name}.")
    source.displayToOthers("${source.name} looks at ${target.name}'s ${part.name}.")
}

suspend fun describeBodyPartDetailed(source: Player, target: Thing, part: BodyPart) {
    describeBodyPart(source, target, part)
    val subject = target.asSubject(source)
    val items = part.getEquipped()
    if (items.isEmpty()){
        val have = target.ifYouWord(source, "don't have", "doesn't have")
        source.displayToMe("$subject $have anything equipped to ${part.name}.")
        return
    }
    val itemList = items.joinToString("\n\t") { "${it.name} equipped ${target.body.getEquippedTarget(it)?.toEquipTarget() ?: "unknown"}" }
    val has = target.ifYouWord(source, "have", "has")
    source.displayToMe("$subject $has the following items equipped to ${part.name}:\n\t$itemList")
}

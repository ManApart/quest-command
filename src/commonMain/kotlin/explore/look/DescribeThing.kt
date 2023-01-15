package explore.look

import core.Player
import core.history.displayToMe
import core.history.displayToOthers
import core.thing.Thing
import core.utility.joinToStringAnd

suspend fun describeThing(source: Player, thing: Thing) {
    var message = thing.getDisplayName()
    message += "\n\t${thing.description}"
    source.displayToMe(message)
    source.displayToOthers("${source.name} looks at ${thing.name}.")
}

suspend fun describeThingDetailed(source: Player, thing: Thing) {
    var message = thing.getDisplayName()
    message += "\n\t${thing.description}"
    message += describeStatusEffects(thing)
    message += describeEquipSlots(thing)
    message += describeProperties(thing)
    source.displayToMe(message)
    source.displayToOthers("${source.name} examines ${thing.name}.")
}

private fun describeStatusEffects(thing: Thing): String {
    if (thing.soul.getConditions().isNotEmpty()) {
        val effects = thing.soul.getConditions().joinToString(", ") { it.name }
        return "\n\t${thing.name} is $effects."
    }
    return ""
}

private fun describeProperties(thing: Thing): String {
    if (!thing.properties.isEmpty()) {
        return "\n\tTags: ${thing.properties.tags}" +
                "\n\tValues: ${thing.properties.values}"
    }
    return ""
}

private fun describeEquipSlots(thing: Thing): String {
    if (thing.equipSlots.isNotEmpty()) {
        val slots = thing.equipSlots.joinToString(" or ") { it.attachPoints.joinToStringAnd() }
        return "\n\tIt can be equipped to $slots."
    }
    return ""
}
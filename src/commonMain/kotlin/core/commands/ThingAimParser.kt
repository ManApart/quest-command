package core.commands

import core.history.displayToMe
import core.thing.Thing
import core.utility.NameSearchableList
import traveling.location.location.Location
import traveling.position.ThingAim

//TODO - allow for response requests?
suspend fun parseThingsFromInventory(source: Thing, arguments: List<String>): List<ThingAim> {
    val things = NameSearchableList(source.inventory.getAllItems())
    return parseThings(source, arguments, things)
}

suspend fun parseThingsFromLocation(source: Thing, arguments: List<String>): List<ThingAim> {
    val things = source.currentLocation().getThings()
    return parseThings(source, arguments, things)
}

suspend fun parseThings(source: Thing, arguments: List<String>, things: NameSearchableList<Thing>): List<ThingAim> {
    val args = Args(arguments, delimiters = listOf("and"))
    return args.getBaseAndGroups("and").mapNotNull { parseThingsInGroup(source, it, things) }
}

private suspend fun parseThingsInGroup(source: Thing, arguments: List<String>, things: NameSearchableList<Thing>): ThingAim? {
    val args = Args(arguments, delimiters = listOf("of"))
    return when {
        args.hasBase() && args.hasGroup("of") -> parseThingAndParts(args, things)
        args.hasBase() -> parseThingOnly(args.fullString, things)
        else -> {
            source.displayToMe("Could not parse things for: ${arguments.joinToString(" ")}")
            null
        }
    }
}

private fun parseThingOnly(name: String, things: NameSearchableList<Thing>): ThingAim? {
    val thing = parseThing(name, things)
    return if (thing != null) {
        ThingAim(thing)
    } else {
        null
    }
}

private suspend fun parseThingAndParts(args: Args, things: NameSearchableList<Thing>): ThingAim? {
    val thing = parseThing(args.getString("of"), things)
    if (thing != null) {
        val parts = parseBodyParts(thing, args.getGroup("base"))
        return ThingAim(thing, parts)
    }
    return null
}

private fun parseThing(name: String, things: NameSearchableList<Thing>): Thing? {
    //TODO - clarify if too many or too few things
    return things.getOrNull(name)
}

suspend fun parseBodyParts(thing: Thing, names: List<String>): List<Location> {
    if (names.size == 1 && (names.first().lowercase() == "all" || names.first().lowercase() == "body")) {
        return thing.body.getParts()
    }
    return thing.body.getAnyParts(names)
}
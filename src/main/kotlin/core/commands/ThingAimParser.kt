package core.commands

import core.history.display
import core.thing.Thing
import core.utility.NameSearchableList
import traveling.location.location.Location
import traveling.position.ThingAim

//TODO - allow for response requests?
fun parseThingsFromInventory(source: Thing, arguments: List<String>, thing: Thing): List<ThingAim> {
    val args = Args(arguments, delimiters = listOf("and"))
    val things = NameSearchableList(thing.inventory.getAllItems())
    return args.getBaseAndGroups("and").mapNotNull { getThing(source, it, things) }
}

//TODO - make location paramatized
fun parseThings(source: Thing, arguments: List<String>): List<ThingAim> {
    val args = Args(arguments, delimiters = listOf("and"))
    val things = source.currentLocation().getThings()
    return args.getBaseAndGroups("and").mapNotNull { getThing(source, it, things) }
}

private fun getThing(source: Thing, arguments: List<String>, things: NameSearchableList<Thing>): ThingAim? {
    val args = Args(arguments, delimiters = listOf("of"))
    return when {
        args.hasBase() && args.hasGroup("of") -> parseThingAndParts(args, things)
        args.hasBase() -> parseThingOnly(args.fullString, things)
        else -> {
            source.display("Could not parse things for: ${arguments.joinToString(" ")}")
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

private fun parseThingAndParts(args: Args, things: NameSearchableList<Thing>): ThingAim? {
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

fun parseBodyParts(thing: Thing, names: List<String>): List<Location> {
    if (names.size == 1 && names.first().lowercase() == "all") {
        return thing.body.getParts()
    }
    return thing.body.getAnyParts(names)
}
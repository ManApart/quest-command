package core.commands

import core.body.BodyPart
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

fun parseThings(source: Thing, arguments: List<String>, things: NameSearchableList<Thing>): List<ThingAim> {
    val args = Args(arguments, delimiters = listOf("and"))
    return args.getBaseAndGroups("and").mapNotNull { parseThingsInGroup(source, it, things) }
}

private fun parseThingsInGroup(source: Thing, arguments: List<String>, things: NameSearchableList<Thing>): ThingAim? {
    val args = Args(arguments, delimiters = listOf("of"))
    return when {
        args.hasBase() && args.hasGroup("of") -> parseThingAndParts(args, things)
        args.hasBase() -> parseThingOnly(args.fullString, things)
        else -> {
            source.displayToMe("Could not parse things for: ${arguments.joinToString(" ")}")
            null
        }
    } ?: parseByPart(args, things)
}

private fun parseThingOnly(name: String, things: NameSearchableList<Thing>): ThingAim? {
    return parseThing(name, things)?.let { ThingAim(it) }
}

private fun parseThingAndParts(args: Args, things: NameSearchableList<Thing>): ThingAim? {
    val thing = parseThing(args.getString("of"), things)
    if (thing != null) {
        val parts = parseBodyParts(thing, args.getGroup("base"))
        return ThingAim(thing, parts)
    }
    return null
}

/**
 Search all things for a part with the given name
 Only return if only one thing has parts with that name
 */
private fun parseByPart(args: Args, things: NameSearchableList<Thing>): ThingAim? {
    val partName = args.getBaseString()
    return things.mapNotNull { t -> t.body.parts.getAll(partName).takeIf { it.isNotEmpty() }
        ?.let { ThingAim(t, it) } }.takeIf { it.size == 1 }?.first()
}

private fun parseThing(name: String, things: NameSearchableList<Thing>): Thing? {
    //TODO - clarify if too many or too few things
    return things.getOrNull(name)
}

fun parseBodyParts(thing: Thing, names: List<String>): List<BodyPart> {
    if (names.size == 1 && (names.first().lowercase() == "all" || names.first().lowercase() == "body")) {
        return thing.body.parts
    }
    return thing.body.parts.getAny(names)
}

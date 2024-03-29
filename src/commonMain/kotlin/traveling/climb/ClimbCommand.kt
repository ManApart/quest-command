package traveling.climb

import core.Player
import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing
import core.utility.NameSearchableList
import core.utility.filterList
import traveling.direction.Direction
import traveling.direction.getDirection
import traveling.location.network.LocationNode

class ClimbCommand : Command() {

    private class ClimbOption(val thing: Thing, val direction: Direction)

    override fun getAliases(): List<String> {
        return listOf("Climb", "cl", "scale", "descend")
    }

    override fun getDescription(): String {
        return "Climb over obstacles"
    }

    override fun getManual(): String {
        return """
	Climb <part> of <thing> - Climb (onto) the thing
	Climb <direction> - Continue climbing in <direction>
	Climb to <part> - Climb to <part>
	Climb s - The s flag silences travel, meaning a minimum amount of output"""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() && source.thing.climbThing == null -> source.getPerceivedThingNames() + source.getPerceivedPartNames()
            args.isEmpty() -> Direction.values().map { it.name } + listOf("to")
            args.last() == "to" -> source.getPerceivedPartNames()
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("of", "to")))
        val arguments = Args(args, delimiters)
        when {
            source.thing.getEncumbrance() >= 1 -> source.displayToMe("You are too encumbered to climb.")
            source.properties.values.getBoolean(IS_CLIMBING) -> processClimbing(
                source,
                keyword,
                arguments,
                source.thing.climbThing!!
            )
            else -> processNewClimb(source, keyword, arguments)
        }
    }

    private suspend fun processNewClimb(player: Player, keyword: String, arguments: Args) {
        val source = player.thing
        val thingName = if (arguments.getString("to") != "") {
            arguments.getString("to")
        } else {
            arguments.getBaseString()
        }
        val things = findAllThings(source)
        val desiredDirection = arguments.getDirection()
        val matchByName = things.getOrNull(thingName)
        val matchByDirection = getDirectionMatches(source, things, desiredDirection)
        val confidentMatch = getConfidentMatch(matchByName, matchByDirection)
        val quiet = arguments.hasFlag("s")

        if (confidentMatch != null) {
            if (!confidentMatch.properties.tags.has("Climbable")) {
                source.displayToMe("${confidentMatch.name} cannot be climbed.")
            } else {
                val parts = getEntryPoints(source, confidentMatch)
                when {
                    parts.isEmpty() -> source.displayToMe("${confidentMatch.name} does not have any parts to climb.")
                    parts.size == 1 -> EventManager.postEvent(
                        AttemptClimbEvent(
                            source,
                            confidentMatch,
                            parts.first(),
                            getDirection(source, desiredDirection, confidentMatch, parts.first()),
                            quiet
                        )
                    )
                    parts.size > 1 -> clarifyClimbPart(player, source.location, confidentMatch)
                }
            }
        } else if (isAlias(keyword) && things.size == 1) {
            val match = things.first()
            val parts = getEntryPoints(source, match)
            EventManager.postEvent(
                AttemptClimbEvent(
                    source,
                    match,
                    parts.first(),
                    getDirection(source, desiredDirection, match, parts.first()),
                    quiet
                )
            )
        } else {
            clarifyClimbThing(player, things, desiredDirection)
        }
    }

    private fun getConfidentMatch(namedMatch: Thing?, directionMatches: List<ClimbOption>): Thing? {
        return namedMatch ?: if (directionMatches.size == 1) {
            directionMatches.first().thing
        } else {
            null
        }
    }

    private suspend fun findAllThings(source: Thing): NameSearchableList<Thing> {
        val localClimbableThings = source.currentLocation().findThingsByTag("Climbable")
        val connections = source.location.getNeighborConnections().filter { connection ->
            localClimbableThings.none { it.name == connection.source.thingName }
                    && connection.destination.hasThingAndPart()
        }
        val connectedThings =
            connections.map { it.destination.location.getLocation().getThings(it.destination.thingName!!) }.flatten()
        return NameSearchableList(localClimbableThings + connectedThings)
    }

    private suspend fun getDirectionMatches(
        player: Thing,
        things: NameSearchableList<Thing>,
        desiredDirection: Direction
    ): List<ClimbOption> {
        return things
            .filterList { getEntryPoints(player, it).isNotEmpty() }
            .map { ClimbOption(it, getDirection(player, it, getEntryPoints(player, it).first())) }
            .filter { it.direction == desiredDirection }
    }

    private suspend fun clarifyClimbThing(player: Player, options: NameSearchableList<Thing>, desiredDirection: Direction) {
        val climbOptions = options
            .filterList { getEntryPoints(player.thing, it).isNotEmpty() }
            .map { ClimbOption(it, getDirection(player.thing, it, getEntryPoints(player.thing, it).first())) }
            .filter { desiredDirection == Direction.NONE || it.direction == desiredDirection }

        when {
            climbOptions.isEmpty() -> player.displayToMe("There doesn't seem to be anything to climb.")
            climbOptions.size == 1 && desiredDirection != Direction.NONE -> CommandParsers.parseCommand(
                player,
                "climb $desiredDirection ${options[0]}"
            )
            options.size == 1 && options.size == climbOptions.size && options.first().name == climbOptions.first().thing.name -> player.displayToMe(
                "Unable to climb ${options.first().name}."
            )
            climbOptions.size == 1 -> CommandParsers.parseCommand(player, "climb ${options[0]}")
            desiredDirection != Direction.NONE -> player.respond("There is nothing to climb.") {
                message("Climb what?")
                displayedOptions(climbOptions.map { "${it.thing.name} (${it.direction})" })
                options(climbOptions.map { "climb ${it.direction} ${it.thing.name}" })
            }
            else -> player.respond("There is nothing to climb.") {
                message("Climb what?")
                optionsNamed(options)
                command { "climb $it" }
            }
        }
    }

    private suspend fun clarifyClimbPart(player: Player, currentLocation: LocationNode, thing: Thing) {
        val options = getAvailableOptions(player.thing, currentLocation, thing)

        if (options.isEmpty()) {
            player.displayToMe("${thing.name} doesn't seem to have anything to climb.")
        } else {
            player.respond("No parts of ${thing.name} to climb.") {
                message("Climb what part of ${thing.name}?")
                optionsNamed(options)
                command { "climb $it of ${thing.name}" }
            }
        }
    }

    private suspend fun getAvailableOptions(player: Thing, currentLocation: LocationNode, thing: Thing): List<LocationNode> {
        //If in same network, all neighbor nodes available, otherwise only entry points
        return if (currentLocation.network == thing.body.layout) {
            currentLocation.getNeighbors()
        } else {
            getEntryPoints(player, thing)
        }
    }

    private suspend fun getEntryPoints(player: Thing, thing: Thing): List<LocationNode> {
        val sourceConnection = player.location.getNeighborConnections()
            .firstOrNull { it.source.hasThingAndPart() && it.source.thingName == thing.name }
        val destConnection = player.location.getNeighborConnections()
            .firstOrNull { it.destination.hasThingAndPart() && it.destination.thingName == thing.name }

        return if (thing.body.getParts().size > 1 && thing.location == player.location) {
            thing.body.getClimbEntryParts()
        } else if (sourceConnection != null && thing.body.hasPart(sourceConnection.source.partName ?: "")) {
            listOf(thing.body.getPartLocation(sourceConnection.source.partName!!))
        } else if (destConnection != null && thing.body.hasPart(destConnection.destination.partName ?: "")) {
            listOf(thing.body.getPartLocation(destConnection.destination.partName!!))
        } else {
            thing.body.getClimbEntryParts()
        }
    }

    private suspend fun processClimbing(player: Player, keyword: String, arguments: Args, thing: Thing) {
        val direction = arguments.getDirection()
        when {
            arguments.isEmpty() -> {
                val keywordDirection = if (keyword == "descend") {
                    Direction.BELOW
                } else {
                    Direction.ABOVE
                }
                climbInDirection(player, keywordDirection, thing)
            }
            direction != Direction.NONE -> climbInDirection(player, direction, thing)
            else -> climbToPart(player, arguments, thing)
        }
    }

    private suspend fun climbInDirection(player: Player, direction: Direction, thing: Thing) {
        val location = player.location.getNeighbors(direction).firstOrNull()
        if (location != null) {
            EventManager.postEvent(AttemptClimbEvent(player.thing, thing, location, direction))
        } else {
            clarifyClimbPart(player, player.location, thing)
        }
    }

    private suspend fun climbToPart(player: Player, arguments: Args, thing: Thing) {
        val partArgs = arguments.getBaseString()
        if (thing.body.hasPart(partArgs)) {
            val part = thing.body.getPartLocation(partArgs)
            val direction = getDirection(player.thing, thing, part)
            EventManager.postEvent(AttemptClimbEvent(player.thing, thing, part, direction))
        } else {
            clarifyClimbPart(player, player.location, thing)
        }
    }


}
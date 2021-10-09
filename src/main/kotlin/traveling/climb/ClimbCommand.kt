package traveling.climb

import core.commands.*
import core.events.EventManager
import core.history.display
import core.history.displayYou
import core.properties.IS_CLIMBING
import core.target.Target
import core.utility.NameSearchableList
import traveling.direction.Direction
import traveling.direction.getDirection
import traveling.location.network.LocationNode

class ClimbCommand : Command() {

    private class ClimbOption(val target: Target, val direction: Direction)

    override fun getAliases(): List<String> {
        return listOf("Climb", "cl", "scale", "descend")
    }

    override fun getDescription(): String {
        return "Climb over obstacles"
    }

    override fun getManual(): String {
        return """
	Climb <part> of <target> - Climb (onto) the target
	Climb <direction> - Continue climbing in <direction>
	Climb to <part> - Climb to <part>
	Climb s - The s flag silences travel, meaning a minimum amount of output"""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("of", "to")))
        val arguments = Args(args, delimiters)
        when {
            source.getEncumbrance() >= 1 -> source.displayYou("You are too encumbered to climb.")
            source.properties.values.getBoolean(IS_CLIMBING) -> processClimbing(source, keyword, arguments, source.climbTarget!!)
            else -> processNewClimb(source, arguments)
        }
    }

    private fun processNewClimb(source: Target, arguments: Args) {
        val targetName = if (arguments.getString("to") != "") {
            arguments.getString("to")
        } else {
            arguments.getBaseString()
        }
        val targets = findAllTargets(source)
        val desiredDirection = arguments.getDirection()
        val matchByName = targets.getOrNull(targetName)
        val matchByDirection = getDirectionMatches(source, targets, desiredDirection)
        val confidentMatch = getConfidentMatch(matchByName, matchByDirection)
        val quiet = arguments.hasFlag("s")

        if (confidentMatch != null) {
            if (!confidentMatch.properties.tags.has("Climbable")) {
                source.displayYou("${confidentMatch.name} cannot be climbed.")
            } else {
                val parts = getEntryPoints(source, confidentMatch)
                when {
                    parts.isEmpty() -> source.displayYou("${confidentMatch.name} does not have any parts to climb.")
                    parts.size == 1 -> EventManager.postEvent(AttemptClimbEvent(source, confidentMatch, parts.first(), getDirection(source, desiredDirection, confidentMatch, parts.first()), quiet))
                    parts.size > 1 -> clarifyClimbPart(source, source.location, confidentMatch)
                }
            }
        } else {
            clarifyClimbTarget(source, targets, desiredDirection)
        }
    }

    private fun getConfidentMatch(namedMatch: Target?, directionMatches: List<ClimbOption>): Target? {
        return namedMatch ?: if (directionMatches.size == 1) {
            directionMatches.first().target
        } else {
            null
        }
    }

    private fun findAllTargets(source: Target): NameSearchableList<Target> {
        val localClimbableTargets = source.currentLocation().findTargetsByTag("Climbable")
        val connections = source.location.getNeighborConnections().filter { connection ->
            localClimbableTargets.none { it.name == connection.source.targetName }
                    && connection.destination.hasTargetAndPart()
        }
        val connectedTargets = connections.map { it.destination.location.getLocation().getTargets(it.destination.targetName!!) }.flatten()
        return NameSearchableList(localClimbableTargets + connectedTargets)
    }

    private fun getDirectionMatches(player: Target, targets: NameSearchableList<Target>, desiredDirection: Direction): List<ClimbOption> {
        return targets.asSequence()
                .filter { getEntryPoints(player, it).isNotEmpty() }
                .map { ClimbOption(it, getDirection(player, it, getEntryPoints(player, it).first())) }
                .filter { it.direction == desiredDirection }
                .toList()
    }

    private fun clarifyClimbTarget(player: Target, options: NameSearchableList<Target>, desiredDirection: Direction) {
        val climbOptions = options.asSequence()
                .filter { getEntryPoints(player, it).isNotEmpty() }
                .map { ClimbOption(it, getDirection(player, it, getEntryPoints(player, it).first())) }
                .filter { desiredDirection == Direction.NONE || it.direction == desiredDirection }
                .toList()

        when {
            climbOptions.isEmpty() -> player.displayYou("There doesn't seem to be anything to climb.")
            climbOptions.size == 1 && desiredDirection != Direction.NONE -> CommandParser.parseCommand("climb $desiredDirection ${options[0]}")
            climbOptions.size == 1 -> CommandParser.parseCommand("climb ${options[0]}")
            desiredDirection != Direction.NONE -> {
                val message = "Climb what?\n\t${climbOptions.joinToString { "${it.target.name} (${it.direction})" }}"
                val response = ResponseRequest(message, (climbOptions.map { it.target.name to "climb ${it.direction} ${it.target.name}" } +
                        climbOptions.map { "${it.target.name} (${it.direction})" to "climb ${it.direction} ${it.target.name}" }
                        ).toMap())
                CommandParser.setResponseRequest(response)
            }
            else -> {
                val message = "Climb what?\n\t${options.joinToString(", ")}"
                val response = ResponseRequest(message, options.associate { it.name to "climb ${it.name}" })
                CommandParser.setResponseRequest(response)
            }
        }
    }

    private fun clarifyClimbPart(player: Target, currentLocation: LocationNode, target: Target) {
        val options = getAvailableOptions(player, currentLocation, target)

        if (options.isEmpty()) {
            player.displayYou("${target.name} doesn't seem to have anything to climb.")
        } else {
            val message = "Climb what part of ${target.name}?\n\t${options.joinToString(", ")}"
            val response = ResponseRequest(message,
                options.associate { it.name to "climb ${it.name} of ${target.name}" })
            CommandParser.setResponseRequest(response)
        }
    }

    private fun getAvailableOptions(player: Target, currentLocation: LocationNode, target: Target): List<LocationNode> {
        //If in same network, all neighbor nodes available, otherwise only entry points
        return if (currentLocation.network == target.body.layout) {
            currentLocation.getNeighbors()
        } else {
            getEntryPoints(player, target)
        }
    }

    private fun getEntryPoints(player: Target, target: Target): List<LocationNode> {
        val sourceConnection = player.location.getNeighborConnections().firstOrNull { it.source.hasTargetAndPart() && it.source.targetName == target.name }
        val destConnection = player.location.getNeighborConnections().firstOrNull { it.destination.hasTargetAndPart() && it.destination.targetName == target.name }

        return if (target.body.getParts().size > 1 && target.location == player.location) {
            target.body.getClimbEntryParts()
        } else if (sourceConnection != null && target.body.hasPart(sourceConnection.source.partName ?: "")) {
            listOf(target.body.getPartLocation(sourceConnection.source.partName!!))
        } else if (destConnection != null && target.body.hasPart(destConnection.destination.partName ?: "")) {
            listOf(target.body.getPartLocation(destConnection.destination.partName!!))
        } else {
            target.body.getClimbEntryParts()
        }
    }

    private fun processClimbing(player: Target, keyword: String, arguments: Args, target: Target) {
        val direction = arguments.getDirection()
        when {
            arguments.isEmpty() -> {
                val keywordDirection = if (keyword == "descend") {
                    Direction.BELOW
                } else {
                    Direction.ABOVE
                }
                climbInDirection(player, keywordDirection, target)
            }
            direction != Direction.NONE -> climbInDirection(player, direction, target)
            else -> climbToPart(player, arguments, target)
        }
    }

    private fun climbInDirection(player: Target, direction: Direction, target: Target) {
        val location = player.location.getNeighbors(direction).firstOrNull()
        if (location != null) {
            EventManager.postEvent(AttemptClimbEvent(player, target, location, direction))
        } else {
            clarifyClimbPart(player, player.location, target)
        }
    }

    private fun climbToPart(player: Target, arguments: Args, target: Target) {
        val partArgs = arguments.getBaseString()
        if (target.body.hasPart(partArgs)) {
            val part = target.body.getPartLocation(partArgs)
            val direction = getDirection(player, target, part)
            EventManager.postEvent(AttemptClimbEvent(player, target, part, direction))
        } else {
            clarifyClimbPart(player, player.location, target)
        }
    }


}
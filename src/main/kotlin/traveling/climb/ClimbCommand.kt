package traveling.climb

import core.GameState
import core.commands.*
import core.events.EventManager
import core.history.display
import core.properties.IS_CLIMBING
import core.target.Target
import core.utility.NameSearchableList
import traveling.direction.Direction
import traveling.direction.getDirection
import traveling.location.location.LocationNode

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

    override fun execute(keyword: String, args: List<String>) {
        val delimiters = listOf(ArgDelimiter(listOf("of", "to")))
        val arguments = Args(args, delimiters)
        when {
            GameState.player.getEncumbrance() >= 1 -> display("You are too encumbered to climb.")
            GameState.player.properties.values.getBoolean(IS_CLIMBING) -> processClimbing(keyword, arguments, GameState.player.climbTarget!!)
            else -> processNewClimb(arguments)
        }
    }

    private fun processNewClimb(arguments: Args) {
        val targetName = if (arguments.getString("to") != "") {
            arguments.getString("to")
        } else {
            arguments.getBaseString()
        }
        val targets = findAllTargets()
        val desiredDirection = arguments.getDirection()
        val matchByName = targets.getOrNull(targetName)
        val matchByDirection = getDirectionMatches(targets, desiredDirection)
        val confidentMatch = getConfidentMatch(matchByName, matchByDirection)
        val quiet = arguments.hasFlag("s")

        if (confidentMatch != null) {
            if (!confidentMatch.properties.tags.has("Climbable")) {
                display("${confidentMatch.name} cannot be climbed.")
            } else {
                val parts = getEntryPoints(confidentMatch)
                when {
                    parts.isEmpty() -> display("${confidentMatch.name} does not have any parts to climb.")
                    parts.size == 1 -> EventManager.postEvent(AttemptClimbEvent(GameState.player, confidentMatch, parts.first(), getDirection(desiredDirection, confidentMatch, parts.first()), quiet))
                    parts.size > 1 -> clarifyClimbPart(GameState.player.location, confidentMatch)
                }
            }
        } else {
            clarifyClimbTarget(targets, desiredDirection)
        }
    }

    private fun getConfidentMatch(namedMatch: Target?, directionMatches: List<ClimbOption>): Target? {
        return namedMatch ?: if (directionMatches.size == 1) {
            directionMatches.first().target
        } else {
            null
        }
    }

    private fun findAllTargets(): NameSearchableList<Target> {
        val localClimbableTargets = GameState.currentLocation().findTargetsByTag("Climbable")
        val connections = GameState.player.location.getNeighborConnections().filter { connection ->
            localClimbableTargets.none { it.name == connection.source.targetName }
                    && connection.destination.hasTargetAndPart()
        }
        val connectedTargets = connections.map { it.destination.location.getLocation().getTargets(it.destination.targetName!!) }.flatten()
        return NameSearchableList(localClimbableTargets + connectedTargets)
    }

    private fun getDirectionMatches(targets: NameSearchableList<Target>, desiredDirection: Direction): List<ClimbOption> {
        return targets.asSequence()
                .filter { getEntryPoints(it).isNotEmpty() }
                .map { ClimbOption(it, getDirection(it, getEntryPoints(it).first())) }
                .filter { it.direction == desiredDirection }
                .toList()
    }

    private fun clarifyClimbTarget(options: NameSearchableList<Target>, desiredDirection: Direction) {
        val climbOptions = options.asSequence()
                .filter { getEntryPoints(it).isNotEmpty() }
                .map { ClimbOption(it, getDirection(it, getEntryPoints(it).first())) }
                .filter { desiredDirection == Direction.NONE || it.direction == desiredDirection }
                .toList()

        when {
            climbOptions.isEmpty() -> display("There doesn't seem to be anything to climb.")
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

    private fun clarifyClimbPart(currentLocation: LocationNode, target: Target) {
        val options = getAvailableOptions(currentLocation, target)

        if (options.isEmpty()) {
            display("${target.name} doesn't seem to have anything to climb.")
        } else {
            val message = "Climb what part of ${target.name}?\n\t${options.joinToString(", ")}"
            val response = ResponseRequest(message,
                options.associate { it.name to "climb ${it.name} of ${target.name}" })
            CommandParser.setResponseRequest(response)
        }
    }

    private fun getAvailableOptions(currentLocation: LocationNode, target: Target): List<LocationNode> {
        //If in same network, all neighbor nodes available, otherwise only entry points
        return if (currentLocation.network == target.body.layout) {
            currentLocation.getNeighbors()
        } else {
            getEntryPoints(target)
        }
    }

    private fun getEntryPoints(target: Target): List<LocationNode> {
        val sourceConnection = GameState.player.location.getNeighborConnections().firstOrNull { it.source.hasTargetAndPart() && it.source.targetName == target.name }
        val destConnection = GameState.player.location.getNeighborConnections().firstOrNull { it.destination.hasTargetAndPart() && it.destination.targetName == target.name }

        return if (target.body.getParts().size > 1 && target.location == GameState.player.location) {
            target.body.getClimbEntryParts()
        } else if (sourceConnection != null && target.body.hasPart(sourceConnection.source.partName ?: "")) {
            listOf(target.body.getPartLocation(sourceConnection.source.partName!!))
        } else if (destConnection != null && target.body.hasPart(destConnection.destination.partName ?: "")) {
            listOf(target.body.getPartLocation(destConnection.destination.partName!!))
        } else {
            target.body.getClimbEntryParts()
        }
    }

    private fun processClimbing(keyword: String, arguments: Args, target: Target) {
        val direction = arguments.getDirection()
        when {
            arguments.isEmpty() -> {
                val keywordDirection = if (keyword == "descend") {
                    Direction.BELOW
                } else {
                    Direction.ABOVE
                }
                climbInDirection(keywordDirection, target)
            }
            direction != Direction.NONE -> climbInDirection(direction, target)
            else -> climbToPart(arguments, target)
        }
    }

    private fun climbInDirection(direction: Direction, target: Target) {
        val location = GameState.player.location.getNeighbors(direction).firstOrNull()
        if (location != null) {
            EventManager.postEvent(AttemptClimbEvent(GameState.player, target, location, direction))
        } else {
            clarifyClimbPart(GameState.player.location, target)
        }
    }

    private fun climbToPart(arguments: Args, target: Target) {
        val partArgs = arguments.getBaseString()
        if (target.body.hasPart(partArgs)) {
            val part = target.body.getPartLocation(partArgs)
            val direction = getDirection(target, part)
            EventManager.postEvent(AttemptClimbEvent(GameState.player, target, part, direction))
        } else {
            clarifyClimbPart(GameState.player.location, target)
        }
    }


}
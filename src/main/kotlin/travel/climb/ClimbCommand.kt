package travel.climb

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.*
import core.gameState.Target
import core.gameState.location.LocationNode
import core.history.display
import core.utility.NameSearchableList
import interact.scope.ScopeManager
import system.EventManager

class ClimbCommand : Command() {

    private class ClimbOption(val target: Target, val direction: Direction)

    override fun getAliases(): Array<String> {
        return arrayOf("Climb", "c", "Scale")
    }

    override fun getDescription(): String {
        return "Climb:\n\tClimb over obstacles"
    }

    override fun getManual(): String {
        return "\n\tClimb <part> of <target> - Climb (onto) the target" +
                "\n\tClimb <direction> - Continue climbing in <direction>" +
                "\n\tClimb to <part> - Climb to <part>"
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("of", "to"))
        if (GameState.player.isClimbing) {
            processClimbing(arguments, GameState.player.climbTarget!!)
        } else {
            processNewClimb(arguments)
        }
    }

    private fun processNewClimb(arguments: Args) {
        val targetName = arguments.argStrings.last()
        val targets = findAllTargets()
        val desiredDirection = arguments.getDirection()
        val matchByName = targets.getOrNull(targetName)
        val matchByDirection = getDirectionMatches(targets, desiredDirection)
        val confidentMatch = getConfidentMatch(matchByName, matchByDirection)

        if (confidentMatch != null) {
            if (!confidentMatch.properties.tags.has("Climbable")) {
                display("${confidentMatch.name} cannot be climbed.")
            } else {
                val parts = getEntryPoints(confidentMatch)
                when {
                    parts.isEmpty() -> display("${confidentMatch.name} does not have any parts to climb.")
                    parts.size == 1 -> EventManager.postEvent(AttemptClimbEvent(GameState.player, confidentMatch, parts.first(), getDirection(desiredDirection, confidentMatch, parts.first())))
                    parts.size > 1 -> clarifyClimbPart(confidentMatch)
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
        val connections = GameState.player.location.getNeighborConnections().filter { it.destination.hasTargetAndPart() }
        val connectedTargets = connections.map { ScopeManager.getScope(it.destination.location).getTargets(it.destination.targetName!!) }.flatten()
        val localClimbableTargets = ScopeManager.getScope().findTargetsByTag("Climbable")
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
                display("Climb what?\n\t${climbOptions.joinToString { "${it.target.name} (${it.direction})" }}")
                val response = ResponseRequest((climbOptions.map { it.target.name to "climb ${it.direction} ${it.target.name}" } +
                        climbOptions.map { "${it.target.name} (${it.direction})" to "climb ${it.direction} ${it.target.name}" }
                        ).toMap())
                CommandParser.responseRequest = response
            }
            else -> {
                display("Climb what?\n\t${options.joinToString(", ")}")
                val response = ResponseRequest(options.map { it.name to "climb ${it.name}" }.toMap())
                CommandParser.responseRequest = response
            }
        }
    }

    private fun clarifyClimbPart(target: Target) {
        val options = getEntryPoints(target)
        if (options.isEmpty()) {
            display("${target.name} doesn't seem to have anything to climb.")
        } else {
            display("Climb what part of ${target.name}?\n\t${options.joinToString(", ")}")
            val response = ResponseRequest(options.map { it.name to "climb ${it.name} of ${target.name}" }.toMap())
            CommandParser.responseRequest = response
        }
    }

    private fun getEntryPoints(target: Target): List<LocationNode> {
        val sourceConnection = GameState.player.location.getNeighborConnections().firstOrNull { it.source.hasTargetAndPart() && it.source.targetName == target.name }
        val destConnection = GameState.player.location.getNeighborConnections().firstOrNull { it.destination.hasTargetAndPart() && it.destination.targetName == target.name }

        return if (sourceConnection != null && target.body.hasPart(sourceConnection.source.partName ?: "")) {
            listOf(target.body.getPartLocation(sourceConnection.source.partName!!))
        }else if (destConnection != null && target.body.hasPart(destConnection.destination.partName ?: "")) {
            listOf(target.body.getPartLocation(destConnection.destination.partName!!))
        } else {
            target.body.getClimbEntryParts()
        }
    }

    private fun processClimbing(arguments: Args, target: Target) {
        val directionNames: List<String> = listOf(Direction.values().map { it.name }, Direction.values().map { it.shortcut }).flatten()
        val direction = arguments.hasAny(directionNames).firstOrNull()
        if (direction != null) {
            climbInDirection(Direction.getDirection(direction), target)
        } else {
            climbToPart(arguments, target)
        }
    }

    private fun climbInDirection(direction: Direction, target: Target) {
        val location = GameState.player.location.getNeighbors(direction).first()
        EventManager.postEvent(AttemptClimbEvent(GameState.player, target, location, direction))
    }

    private fun climbToPart(arguments: Args, target: Target) {
        val filteredArgs = arguments.argsWithout(listOf("to", "of", target.name)).joinToString(" ")
        if (target.body.hasPart(filteredArgs)) {
            val part = target.body.getPartLocation(filteredArgs)
            val direction = GameState.player.location.getConnection(part)?.vector?.direction ?: Direction.NONE
            EventManager.postEvent(AttemptClimbEvent(GameState.player, target, part, direction))
        } else {
            clarifyClimbPart(target)
        }
    }



}
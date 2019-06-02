package travel.climb

import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.Target
import core.gameState.location.LocationNode
import core.history.display
import core.utility.NameSearchableList
import interact.scope.ScopeManager
import system.EventManager

class ClimbCommand : Command() {
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
        val bestMatch = targets.getOrNull(targetName)

        if (bestMatch == null) {
            clarifyClimbTarget(targets)
        } else if (!bestMatch.properties.tags.has("Climbable")) {
            display("${bestMatch.name} cannot be climbed.")
        } else {
            val parts = getEntryPoints(bestMatch)
            when {
                parts.isEmpty() -> display("${bestMatch.name} does not have any parts to climb.")
                parts.size == 1 -> EventManager.postEvent(AttemptClimbEvent(GameState.player, bestMatch, parts.first()))
                parts.size > 1 -> clarifyClimbPart(bestMatch)
            }
        }
    }

    private fun findAllTargets(): NameSearchableList<Target> {
        val connections = GameState.player.location.getNeighborConnections().filter { it.destination.hasTargetAndPart() }
        val connectedTargets = connections.map { ScopeManager.getScope(it.destination.location).getTargets(it.destination.targetName!!) }.flatten()
        val localClimbableTargets = ScopeManager.getScope().findTargetsByTag("Climbable")
        return NameSearchableList( localClimbableTargets + connectedTargets)
    }

    private fun clarifyClimbTarget(options: NameSearchableList<Target>) {
        when {
            options.isEmpty() -> display("There doesn't seem to be anything to climb.")
            options.size == 1 -> CommandParser.parseCommand("climb ${options[0]}")
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
        val connection = GameState.player.location.getNeighborConnections().firstOrNull { it.destination.hasTargetAndPart() && it.destination.targetName == target.name }
        return if (connection != null && target.body.hasPart(connection.destination.partName ?: "")) {
            listOf(target.body.getPartLocation(connection.destination.partName!!))
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
        EventManager.postEvent(AttemptClimbEvent(GameState.player, target, location))
    }

    private fun climbToPart(arguments: Args, target: Target) {
        val filteredArgs = arguments.argsWithout(listOf("to", "of", target.name)).joinToString(" ")
        if (target.body.hasPart(filteredArgs)) {
            val part = target.body.getPartLocation(filteredArgs)
            EventManager.postEvent(AttemptClimbEvent(GameState.player, target, part))
        } else {
            clarifyClimbPart(target)
        }
    }

}
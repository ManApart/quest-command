package traveling.approach

import traveling.position.Distances
import core.GameState
import core.commands.*
import core.events.EventManager
import core.history.display
import core.target.Target
import traveling.move.StartMoveEvent

class ApproachCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Approach", "forward", "advance", "app")
    }

    override fun getDescription(): String {
        return "Move closer to the target."
    }

    override fun getManual(): String {
        return """
	Approach <target> - Move to the target.
	Approach <target> by <amount> - Move closer to the target by a certain amount."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("to"))
        val creatures = GameState.player.location.getLocation().getCreaturesExcludingPlayer()
        val target = determineTarget(arguments, creatures)
        val distance = arguments.getNumber()
        when {
            target != null && distance != null -> approachByAmount(target, distance)
            target != null && keyword.lowercase() == "approach" -> clarifyAmount(target)
            target != null -> EventManager.postEvent(StartMoveEvent(GameState.player, target.position))
            else -> clarifyTarget(creatures)
        }
    }

    private fun determineTarget(args: Args, creatures: List<Target>) : Target? {
        val parsedTarget = parseTargets(args.getBaseGroup()).firstOrNull()?.target ?: GameState.player.ai.aggroTarget
        return when {
            parsedTarget != null -> parsedTarget
            creatures.size == 1 -> creatures.first()
            else -> null
        }
    }

    private fun approachByAmount(target: Target, distance: Int) {
        val goal = GameState.player.position.closer(target.position, distance)
        EventManager.postEvent(StartMoveEvent(GameState.player, goal))
    }

    private fun clarifyAmount(target: Target) {
        val targetRange = GameState.player.position.getDistance(target.position)
        val targets = mapOf("minimum" to Distances.MIN_RANGE, "halfway" to targetRange / 2, "all the way" to targetRange)
        CommandParser.setResponseRequest(ResponseRequest("Move how much?\n\t${targets.keys.joinToString(", ")}",
            targets.entries.associate { it.key to "approach ${target.name} by ${it.value}" }))
    }

    private fun clarifyTarget(creatures: List<Target>) {
        if (creatures.isEmpty()) {
            display("Couldn't find anything to approach.")
        } else {
            val message = "Approach what?\n\t${creatures.joinToString(", ")}"
            val response = ResponseRequest(message, creatures.associate { it.name to "approach ${it.name}" })
            CommandParser.setResponseRequest(response)
        }
    }

}
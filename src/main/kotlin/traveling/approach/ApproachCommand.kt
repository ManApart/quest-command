package traveling.approach

import core.commands.*
import core.events.EventManager
import core.history.display
import core.history.displayYou
import core.target.Target
import traveling.move.StartMoveEvent
import traveling.position.Distances

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

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("to"))
        val creatures = source.location.getLocation().getCreaturesExcludingPlayer(source)
        val target = determineTarget(source, arguments, creatures)
        val distance = arguments.getNumber()
        when {
            target != null && distance != null -> approachByAmount(source, target, distance)
            target != null && keyword.lowercase() == "approach" -> clarifyAmount(source, target)
            target != null -> EventManager.postEvent(StartMoveEvent(source, target.position))
            else -> clarifyTarget(source, creatures)
        }
    }

    private fun determineTarget(source: Target, args: Args, creatures: List<Target>) : Target? {
        val parsedTarget = parseTargets(source, args.getBaseGroup()).firstOrNull()?.target ?: source.ai.aggroTarget
        return when {
            parsedTarget != null -> parsedTarget
            creatures.size == 1 -> creatures.first()
            else -> null
        }
    }

    private fun approachByAmount(source: Target, target: Target, distance: Int) {
        val goal = source.position.closer(target.position, distance)
        EventManager.postEvent(StartMoveEvent(source, goal))
    }

    private fun clarifyAmount(source: Target, target: Target) {
        val targetRange = source.position.getDistance(target.position)
        val targets = mapOf("minimum" to Distances.MIN_RANGE, "halfway" to targetRange / 2, "all the way" to targetRange)
        CommandParser.setResponseRequest(ResponseRequest("Move how much?\n\t${targets.keys.joinToString(", ")}",
            targets.entries.associate { it.key to "approach ${target.name} by ${it.value}" }))
    }

    private fun clarifyTarget(source: Target, creatures: List<Target>) {
        if (creatures.isEmpty()) {
            source.displayYou("Couldn't find anything to approach.")
        } else {
            val message = "Approach what?\n\t${creatures.joinToString(", ")}"
            val response = ResponseRequest(message, creatures.associate { it.name to "approach ${it.name}" })
            CommandParser.setResponseRequest(response)
        }
    }

}
package traveling.approach

import core.GameState
import core.commands.*
import core.events.EventManager
import core.history.display
import core.target.Target
import traveling.move.StartMoveEvent
import traveling.position.Distances.HUMAN_LENGTH

class RetreatCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Retreat", "backward", "back")
    }

    override fun getDescription(): String {
        return "Move further from the target."
    }

    override fun getManual(): String {
        return """
	Retreat from <target> - Move away from the target.
	Retreat from <target> by <amount> - Move away from the target by the amount."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("from", "by"))
        val creatures = source.location.getLocation().getCreaturesExcludingPlayer(source)
        val target = determineTarget(source, arguments, creatures)
        val distance = arguments.getNumber()
        when {
            target != null && distance != null -> retreatByAmount(source, target, distance)
            target != null && keyword.lowercase() == "retreat" -> clarifyAmount(target)
            target != null -> retreatByAmount(source, target, HUMAN_LENGTH)
            else -> clarifyTarget(creatures)
        }
    }

    private fun determineTarget(source: Target, args: Args, creatures: List<Target>) : Target? {
        val parsedTarget = parseTargets(source, args.getBaseGroup()).firstOrNull()?.target ?: parseTargets(source, args.getGroup("from")).firstOrNull()?.target ?: source.ai.aggroTarget
        return when {
            parsedTarget != null -> parsedTarget
            creatures.size == 1 -> creatures.first()
            else -> null
        }
    }

    private fun retreatByAmount(source: Target, target: Target, distance: Int) {
        val goal = source.position.further(target.position, distance)
        EventManager.postEvent(StartMoveEvent(source, goal))
    }

    private fun clarifyAmount(target: Target) {
        val distanceOptions = listOf("1", "3", "5", "10", "50", "#")
        val distanceResponse = ResponseRequest("Retreat how much?\n\t${distanceOptions.joinToString(", ")}",
            distanceOptions.associateWith { "retreat from $target by $it" })
        CommandParser.setResponseRequest(distanceResponse)
    }

    private fun clarifyTarget(creatures: List<Target>) {
        if (creatures.isEmpty()) {
            display("Couldn't find anything to retreat from. You must be really frightened.")
        } else {
            val message = "Retreat from what?\n\t${creatures.joinToString(", ")}"
            val response = ResponseRequest(message, creatures.associate { it.name to "retreat from ${it.name}" })
            CommandParser.setResponseRequest(response)
        }
    }


}
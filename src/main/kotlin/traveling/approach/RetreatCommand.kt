package traveling.approach

import traveling.position.Distances.HUMAN_LENGTH
import core.GameState
import core.commands.*
import core.history.display
import core.events.EventManager
import core.target.Target
import traveling.move.StartMoveEvent

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

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("from", "by"))
        val creatures = GameState.player.location.getLocation().getCreaturesExcludingPlayer()
        val target = determineTarget(arguments, creatures)
        val distance = arguments.getNumber()
        when {
            target != null && distance != null -> retreatByAmount(target, distance)
            target != null && keyword.lowercase() == "retreat" -> clarifyAmount(target)
            target != null -> retreatByAmount(target, HUMAN_LENGTH)
            else -> clarifyTarget(creatures)
        }
    }

    private fun determineTarget(args: Args, creatures: List<Target>) : Target? {
        val parsedTarget = parseTargets(args.getBaseGroup()).firstOrNull()?.target ?: parseTargets(args.getGroup("from")).firstOrNull()?.target ?: GameState.player.ai.aggroTarget
        return when {
            parsedTarget != null -> parsedTarget
            creatures.size == 1 -> creatures.first()
            else -> null
        }
    }

    private fun retreatByAmount(target: Target, distance: Int) {
        val goal = GameState.player.position.further(target.position, distance)
        EventManager.postEvent(StartMoveEvent(GameState.player, goal))
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
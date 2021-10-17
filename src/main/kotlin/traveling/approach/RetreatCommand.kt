package traveling.approach

import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import traveling.move.StartMoveEvent
import traveling.position.Distances.HUMAN_LENGTH

class RetreatCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Retreat", "backward", "back")
    }

    override fun getDescription(): String {
        return "Move further from the thing."
    }

    override fun getManual(): String {
        return """
	Retreat from <thing> - Move away from the thing.
	Retreat from <thing> by <amount> - Move away from the thing by the amount."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("from", "by"))
        val creatures = source.location.getLocation().getCreaturesExcludingPlayer(source)
        val thing = determineThing(source, arguments, creatures)
        val distance = arguments.getNumber()
        when {
            thing != null && distance != null -> retreatByAmount(source, thing, distance)
            thing != null && keyword.lowercase() == "retreat" -> clarifyAmount(thing)
            thing != null -> retreatByAmount(source, thing, HUMAN_LENGTH)
            else -> clarifyThing(source, creatures)
        }
    }

    private fun determineThing(source: Thing, args: Args, creatures: List<Thing>) : Thing? {
        val parsedThing = parseThings(source, args.getBaseGroup()).firstOrNull()?.thing ?: parseThings(source, args.getGroup("from")).firstOrNull()?.thing ?: source.ai.aggroThing
        return when {
            parsedThing != null -> parsedThing
            creatures.size == 1 -> creatures.first()
            else -> null
        }
    }

    private fun retreatByAmount(source: Thing, thing: Thing, distance: Int) {
        val goal = source.position.further(thing.position, distance)
        EventManager.postEvent(StartMoveEvent(source, goal))
    }

    private fun clarifyAmount(thing: Thing) {
        val distanceOptions = listOf("1", "3", "5", "10", "50", "#")
        val distanceResponse = ResponseRequest("Retreat how much?\n\t${distanceOptions.joinToString(", ")}",
            distanceOptions.associateWith { "retreat from $thing by $it" })
        CommandParsers.setResponseRequest(distanceResponse)
    }

    private fun clarifyThing(source: Thing, creatures: List<Thing>) {
        if (creatures.isEmpty()) {
            source.displayToMe("Couldn't find anything to retreat from. You must be really frightened.")
        } else {
            val message = "Retreat from what?\n\t${creatures.joinToString(", ")}"
            val response = ResponseRequest(message, creatures.associate { it.name to "retreat from ${it.name}" })
            CommandParsers.setResponseRequest(response)
        }
    }


}
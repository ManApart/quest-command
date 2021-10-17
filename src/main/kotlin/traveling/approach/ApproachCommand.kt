package traveling.approach

import core.commands.*
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import traveling.move.StartMoveEvent
import traveling.position.Distances

class ApproachCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Approach", "forward", "advance", "app")
    }

    override fun getDescription(): String {
        return "Move closer to the thing."
    }

    override fun getManual(): String {
        return """
	Approach <thing> - Move to the thing.
	Approach <thing> by <amount> - Move closer to the thing by a certain amount."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("to"))
        val creatures = source.location.getLocation().getCreaturesExcludingPlayer(source)
        val thing = determineThing(source, arguments, creatures)
        val distance = arguments.getNumber()
        when {
            thing != null && distance != null -> approachByAmount(source, thing, distance)
            thing != null && keyword.lowercase() == "approach" -> clarifyAmount(source, thing)
            thing != null -> EventManager.postEvent(StartMoveEvent(source, thing.position))
            else -> clarifyThing(source, creatures)
        }
    }

    private fun determineThing(source: Thing, args: Args, creatures: List<Thing>) : Thing? {
        val parsedThing = parseThings(source, args.getBaseGroup()).firstOrNull()?.thing ?: source.ai.aggroThing
        return when {
            parsedThing != null -> parsedThing
            creatures.size == 1 -> creatures.first()
            else -> null
        }
    }

    private fun approachByAmount(source: Thing, thing: Thing, distance: Int) {
        val goal = source.position.closer(thing.position, distance)
        EventManager.postEvent(StartMoveEvent(source, goal))
    }

    private fun clarifyAmount(source: Thing, thing: Thing) {
        val thingRange = source.position.getDistance(thing.position)
        val things = mapOf("minimum" to Distances.MIN_RANGE, "halfway" to thingRange / 2, "all the way" to thingRange)
        CommandParser.setResponseRequest(ResponseRequest("Move how much?\n\t${things.keys.joinToString(", ")}",
            things.entries.associate { it.key to "approach ${thing.name} by ${it.value}" }))
    }

    private fun clarifyThing(source: Thing, creatures: List<Thing>) {
        if (creatures.isEmpty()) {
            source.displayToMe("Couldn't find anything to approach.")
        } else {
            val message = "Approach what?\n\t${creatures.joinToString(", ")}"
            val response = ResponseRequest(message, creatures.associate { it.name to "approach ${it.name}" })
            CommandParser.setResponseRequest(response)
        }
    }

}
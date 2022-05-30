package traveling.approach

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.parseThingsFromLocation
import core.commands.respond
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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("to"))
        val creatures = source.location.getLocation().getCreaturesExcludingPlayer(source.thing)
        val thing = determineThing(source.thing, arguments, creatures)
        val distance = arguments.getNumber()
        when {
            thing != null && distance != null -> approachByAmount(source.thing, thing, distance)
            thing != null && keyword.lowercase() == "approach" -> clarifyAmount(source, thing)
            thing != null -> EventManager.postEvent(StartMoveEvent(source.thing, thing.position))
            else -> clarifyThing(source, creatures)
        }
    }

    private fun determineThing(source: Thing, args: Args, creatures: List<Thing>) : Thing? {
        val parsedThing = parseThingsFromLocation(source, args.getBaseGroup()).firstOrNull()?.thing ?: source.mind.ai.aggroThing
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

    private fun clarifyAmount(source: Player, thing: Thing) {
        val thingRange = source.position.getDistance(thing.position)

        source.respond({}) {
            message("Move how much?")
            displayedOptions("minimum", "halfway", "all the way")
            options(Distances.MIN_RANGE.toString(), (thingRange/2).toString(), thingRange.toString())
            command { "approach ${thing.name} by $it" }
        }
    }

    private fun clarifyThing(source: Player, creatures: List<Thing>) {
        if (creatures.isEmpty()) {
            source.displayToMe("Couldn't find anything to approach.")
        } else {
            source.respond("There is nothing to approach.") {
                message("Approach what?")
                options(creatures)
                command { "approach $it" }
            }
        }
    }

}
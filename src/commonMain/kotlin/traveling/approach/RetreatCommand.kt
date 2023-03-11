package traveling.approach

import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.parseThingsFromLocation
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import traveling.move.startMoveEvent
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

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("from")
            args.last() == "from" -> source.getPerceivedThingNames()
            args.size == 2 -> listOf("by")
            args.last() == "by" -> listOf("1", "5", "10")
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("from", "by"))
        val creatures = source.location.getLocation().getCreaturesExcludingPlayer(source.thing)
        val thing = determineThing(source.thing, arguments, creatures)
        val distance = arguments.getNumber()
        when {
            thing != null && distance != null -> retreatByAmount(source.thing, thing, distance)
            thing != null && keyword.lowercase() == "retreat" -> clarifyAmount(source, thing)
            thing != null -> retreatByAmount(source.thing, thing, HUMAN_LENGTH)
            else -> clarifyThing(source, creatures)
        }
    }

    private suspend fun determineThing(source: Thing, args: Args, creatures: List<Thing>): Thing? {
        val parsedThing = parseThingsFromLocation(source, args.getBaseGroup()).firstOrNull()?.thing ?: parseThingsFromLocation(source, args.getGroup("from")).firstOrNull()?.thing ?: source.mind.getAggroTarget()
        return when {
            parsedThing != null -> parsedThing
            creatures.size == 1 -> creatures.first()
            else -> null
        }
    }

    private suspend fun retreatByAmount(source: Thing, thing: Thing, distance: Int) {
        val goal = source.position.further(thing.position, distance)
        EventManager.postEvent(startMoveEvent(source, goal))
    }

    private fun clarifyAmount(player: Player, thing: Thing) {
        player.respond({}) {
            message("Retreat how much?")
            options("1", "3", "5", "10", "50", "#")
            command { "retreat from $thing by $it" }
        }
    }

    private fun clarifyThing(source: Player, creatures: List<Thing>) {
        if (creatures.isEmpty()) {
            source.displayToMe("Couldn't find anything to retreat from. You must be really frightened.")
        } else {
            source.respond("There is nothing to retreat from.") {
                message("Retreat from what?")
                optionsNamed(creatures)
                command { "retreat from $it" }
            }
        }
    }


}
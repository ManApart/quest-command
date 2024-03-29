package traveling.move

import core.Player
import core.commands.*
import core.events.EventManager
import core.history.display
import core.utility.asSubject
import core.utility.isAre
import traveling.direction.Direction

class MoveCommand : Command() {

    override fun getAliases(): List<String> {
        return listOf("Move", "mv", "walk", "run")
    }

    override fun getDescription(): String {
        return "Move within locations."
    }

    override fun getManual(): String {
        return """
	Move to 0,1 - Move to a specific place within a location.
	Move to 0,1,0 - Move to a specific place within a location, including a height.
	Move to <thing> - Move to a thing within a location.
	Move <distance> towards <direction> - Move a set distance in a direction."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when{
            args.isEmpty() -> listOf("to", "0,0,0", "10")
            args.last() == "to" -> source.getPerceivedThingNames()
            args.last() == "towards" -> Direction.values().map { it.name }
            args.last().toIntOrNull() != null -> listOf("towards")
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        //Move this check to the listener
        if (source.thing.getEncumbrance() >= 1) {
            source.display { "${source.asSubject(it)} ${source.isAre(it)} too encumbered to move." }
        } else {
            val delimiters = listOf("to", "towards")
            val arguments = Args(args, delimiters = delimiters)
            val vector = parseVector(args.removeAll(delimiters))
            val thing = parseThingsFromLocation(source.thing, arguments.getBaseAndStrings("to")).firstOrNull()
            val direction = parseNullableDirection(arguments.getBaseAndStrings("towards"))
            val distance = arguments.getNumber()

            when {
                vector != null -> EventManager.postEvent(startMoveEvent(source.thing, vector))
                thing != null -> EventManager.postEvent(startMoveEvent(source.thing, thing.thing.position))
                distance != null && direction != null -> EventManager.postEvent(startMoveEvent(source.thing, source.position + (direction.vector * distance)))
                direction != null || distance != null || args.isEmpty() -> parseDirectionAndDistance(source, direction, distance)
                //TODO - response request
                else -> source.display("Could not understand: move ${args.joinToString(" ")}")
            }
        }
    }

    private suspend fun parseDirectionAndDistance(source: Player, initialDirection: Direction?, initialDistance: Int?) {
        val clarifier = source.clarify {
            respond("direction") {
                message("Move in what direction?")
                options(Direction.values().map { it.name })
                command { "move ${initialDirection ?: ""} towards $it" }
                value(initialDirection?.name)
                defaultValue(Direction.NORTH.name)
            }
            respond("distance") {
                message("Move how far?")
                options("1", "3", "5", "10", "50", "#")
                command { "move $it towards ${initialDirection.toString()}" }
                value(initialDistance)
                defaultValue(1)
            }
        }

        if (!clarifier.hasAllValues()) {
            clarifier.requestAResponse()
        } else {
            val distance = clarifier.getIntValue("distance")
            val direction = Direction.getDirection(clarifier.getStringValue("direction"))
            val vector = (direction.vector * distance) + source.position
            EventManager.postEvent(startMoveEvent(source.thing, vector))
        }
    }

}
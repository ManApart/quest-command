package traveling.move

import core.commands.*
import core.events.EventManager
import core.history.display
import core.thing.Thing
import core.utility.asSubject
import core.utility.isAre
import traveling.direction.Direction
import traveling.position.NO_VECTOR

//TODO - take optional ignore z
class MoveCommand : Command() {

    override fun getAliases(): List<String> {
        return listOf("Move", "mv", "walk", "run")
    }

    override fun getDescription(): String {
        return "Move within locations."
    }

    override fun getManual(): String {
        return """
	Move to 0,1,0 - Move to a specific place within a location.
	Move to <thing> - Move to a thing within a location.
	Move <distance> towards <direction> - Move a set distance in a direction."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(source: Thing, keyword: String, args: List<String>) {
        //Move this check to the listener
        if (source.getEncumbrance() >= 1) {
            source.display{"${source.asSubject(it)} ${source.isAre(it)} too encumbered to move."}
        } else {
            val arguments = Args(args, delimiters = listOf("to", "towards"))
            val vector = parseVector(args)
            val thing = parseThings(source, arguments.getBaseAndStrings("to")).firstOrNull()
            val direction = parseNullableDirection(arguments.getBaseAndStrings("towards"))
            val distance = arguments.getNumber()
            val useDefault = keyword != "move"

            when {
                vector != NO_VECTOR -> EventManager.postEvent(StartMoveEvent(source, vector))
                thing != null -> EventManager.postEvent(StartMoveEvent(source, thing.thing.position))
                distance != null && direction != null -> EventManager.postEvent(StartMoveEvent(source, source.position.getVectorInDirection(direction.vector, distance)))
                direction != null || distance != null || args.isEmpty() -> parseDirectionAndDistance(source, direction, distance, useDefault)
                //TODO - response request
                else -> source.display("Could not understand: move ${args.joinToString(" ")}")
            }
        }
    }

    private fun parseDirectionAndDistance(source: Thing, initialDirection: Direction?, initialDistance: Int?, useDefault: Boolean) {
        val distanceOptions = listOf("1", "3", "5", "10", "50", "#")
        val distanceResponse = ResponseRequest("Move how far?\n\t${distanceOptions.joinToString(", ")}",
            distanceOptions.associateWith { "move $it towards ${initialDirection.toString()}" })

        val directionOptions = Direction.values().map { it.name }
        val directionResponse = ResponseRequest("Move in what direction?\n\t${directionOptions.joinToString(", ")}",
            directionOptions.associateWith { "move ${initialDirection ?: ""} towards $it" })

        val responseHelper = ResponseRequestHelper(mapOf(
                "direction" to ResponseRequestWrapper(initialDirection?.name, directionResponse, useDefault, Direction.NORTH.name),
                "distance" to ResponseRequestWrapper(initialDistance, distanceResponse, useDefault, 1)
        ))

        if (!responseHelper.hasAllValues()) {
            responseHelper.requestAResponse()
        } else {
            val distance = responseHelper.getIntValue("distance")
            val direction = Direction.getDirection(responseHelper.getStringValue("direction"))
            val vector = (direction.vector * distance) + source.position
            EventManager.postEvent(StartMoveEvent(source, vector))
        }
    }

}
package traveling.move

import core.commands.*
import traveling.direction.Direction
import traveling.position.NO_VECTOR
import core.target.Target
import core.history.display
import core.utility.StringFormatter.getIsAre
import core.utility.StringFormatter.getSubject
import core.events.EventManager

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
	Move to <target> - Move to a target within a location.
	Move <distance> towards <direction> - Move a set distance in a direction."""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        //Move this check to the listener
        if (source.getEncumbrance() >= 1) {
            display("${getSubject(source)} ${getIsAre(source)} too encumbered to move.")
        } else {
            val arguments = Args(args, delimiters = listOf("to", "towards"))
            val vector = parseVector(args)
            val target = parseTargets(arguments.getBaseAndStrings("to")).firstOrNull()
            val direction = parseNullableDirection(arguments.getBaseAndStrings("towards"))
            val distance = arguments.getNumber()
            val useDefault = keyword != "move"

            when {
                vector != NO_VECTOR -> EventManager.postEvent(StartMoveEvent(source, vector))
                target != null -> EventManager.postEvent(StartMoveEvent(source, target.target.position))
                distance != null && direction != null -> EventManager.postEvent(StartMoveEvent(source, source.position.getVectorInDirection(direction.vector, distance)))
                direction != null || distance != null || args.isEmpty() -> parseDirectionAndDistance(source, direction, distance, useDefault)
                //TODO - response request
                else -> display("Could not understand: move ${args.joinToString(" ")}")
            }
        }
    }

    private fun parseDirectionAndDistance(source: Target, initialDirection: Direction?, initialDistance: Int?, useDefault: Boolean) {
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
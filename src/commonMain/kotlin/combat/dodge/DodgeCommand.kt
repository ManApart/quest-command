package combat.dodge

import core.Player
import core.commands.Command
import core.commands.parseDirection
import core.events.EventManager
import core.thing.Thing
import traveling.direction.Direction
import traveling.location.weather.WeatherManager
import traveling.move.StartMoveEvent

class DodgeCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Dodge")
    }

    override fun getDescription(): String {
        return "Attempt to dodge a blow."
    }

    override fun getManual(): String {
        return """
	Dodge <direction> *<distance> - Attempt to dodge a blow. Uses more stamina, but is faster than just moving."""
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> Direction.values().map { it.name }
            else -> listOf()
        }
    }

    override suspend fun execute(source: Thing, keyword: String, args: List<String>) {
        val direction = parseDirection(args).vector * 10
        EventManager.postEvent(StartMoveEvent(source, direction, 2f, 1.5f))
    }


}
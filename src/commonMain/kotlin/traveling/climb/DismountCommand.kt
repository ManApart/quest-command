package traveling.climb

import core.Player
import core.commands.Command
import core.events.EventManager
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing
import traveling.location.location.LocationPoint
import traveling.location.weather.WeatherManager
import traveling.position.NO_VECTOR

class DismountCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Dismount", "dis")
    }

    override fun getDescription(): String {
        return "Stop climbing (only at top or bottom of obstacle)"
    }

    override fun getManual(): String {
        return """
	Dismount -Stop climbing (only at top or bottom of obstacle)"""
    }

    override fun getCategory(): List<String> {
        return listOf("Traveling")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return listOf()
    }

    override suspend fun execute(source: Thing, keyword: String, args: List<String>) {
        if (source.properties.values.getBoolean(IS_CLIMBING)) {
            //If current location has a network connection/ exit, dismount there, otherwise dismount to thing location if height 0
            val exit = getExitLocation(source)
            val climbThing = source.climbThing!!
            val thingLocation = LocationPoint(climbThing.location)
            val part = source.location
            val origin = LocationPoint(climbThing.location, NO_VECTOR, climbThing.name, part.name)

            when {
                exit != null -> EventManager.postEvent(ClimbCompleteEvent(source, source.climbThing!!, origin, exit))
                source.location.getDistanceToLowestNodeInNetwork() == 0 -> EventManager.postEvent(ClimbCompleteEvent(source, source.climbThing!!, origin, thingLocation))
                else -> source.displayToMe("You can't safely dismount from here, but you may be able to jump down.")
            }
        } else {
            source.displayToMe("You're not climbing.")
        }
    }

    private suspend fun getExitLocation(source: Thing) : LocationPoint? {
        val climbThing = source.climbThing!!
        val location = climbThing.location
        val part = climbThing.body.getPart(source.location.name)

        return climbThing.location.getNeighborConnections().firstOrNull {
            it.source.equals(location, climbThing, part)
        }?.destination
    }


}
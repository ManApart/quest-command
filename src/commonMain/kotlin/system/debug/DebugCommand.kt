package system.debug

import core.GameState
import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.parseThingsFromLocation
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import status.stat.StatKind
import traveling.location.weather.WeatherManager

class DebugCommand : Command() {

    override fun getAliases(): List<String> {
        return listOf("Debug", "db")
    }

    override fun getDescription(): String {
        return "Change various settings for testing/cheating."
    }

    override fun getManual(): String {
        return """ 
    Debug - Toggle various debug settings all on or off at once.
    Debug list - View the gamestate's properties.
    Debug lvlreq <on/off> - Toggle the requirement for skills/spells to have a specific level.
    Debug statchanges <on/off> - Toggle whether stats (stamina, focus, health, etc) can be depleted.
    Debug random <on/off> - Toggle random chances always succeeding. 
    Debug map <on/off> - Toggle showing all or just discovered map locations. 
    Debug clarity <on/off> - Toggle total clarity on or off. Total clarity skips perception checks 
    Debug displayupdates <on/off> - Toggle inline updating display messages (for things like progress bars). 
    Debug stat <stat name> <desired level> on *<thing> - Set a stat to the desired level.
    Debug prop <prop name> <desired level> on *<thing> - Set a property to the desired level.
    Debug tag *<remove> <tag name> on *<thing> - Add (or remove) a tag.
    Debug weather <weather name> - Set weather in current location to the given weather, if it exists.
    Debug verbose <on/off> - Toggle all the verbose settings on or off 
        """
    }

    override fun getCategory(): List<String> {
        return listOf("Debugging")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("list", "levelreq", "statchanges", "random", "map", "clarity", "displayupdates", "stat", "prop", "tag", "weather", "aiupdates")
            args.last() in listOf("levelreq", "statchanges", "random", "map", "clarity", "displayupdates", "aiupdates") -> listOf("on", "off")
            args.last() == "weather" -> WeatherManager.getAllWeather().map { it.name }
            args.last() == "on" -> source.getPerceivedThingNames()
            else -> listOf()
        }
    }

    override suspend fun execute(source: Thing, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("on"))

        if (args.isEmpty()) {
            sendDebugToggleEvent(source, arguments, DebugType.LEVEL_REQ, DebugType.STAT_CHANGES, DebugType.RANDOM_SUCCEED)
        } else {
            when (args.first()) {
                "list" -> EventManager.postEvent(DebugListEvent(source))
                "lvlreq" -> sendDebugToggleEvent(source, arguments, DebugType.LEVEL_REQ)
                "statchanges" -> sendDebugToggleEvent(source, arguments, DebugType.STAT_CHANGES)
                "random" -> sendDebugToggleEvent(source, arguments, DebugType.RANDOM_SUCCEED)
                "aiupdates" -> sendDebugToggleEvent(source, arguments, DebugType.VERBOSE_AI)
                "poll" -> sendDebugToggleEvent(source, arguments, DebugType.POLL_CONNECTION)
                "map" -> sendDebugToggleEvent(source, arguments, DebugType.MAP_SHOW_ALL_LOCATIONS)
                "recipe" -> sendDebugToggleEvent(source, arguments, DebugType.RECIPE_SHOW_ALL)
                "clarity" -> sendDebugToggleEvent(source, arguments, DebugType.CLARITY)
                "displayupdates" -> sendDebugToggleEvent(source, arguments, DebugType.DISPLAY_UPDATES)
                "stat" -> sendDebugStatEvent(source, StatKind.LEVELED, arguments)
                "prop" -> sendDebugStatEvent(source, StatKind.PROP_VAL, arguments)
                "tag" -> sendDebugTagEvent(source, arguments)
                "verbose" -> sendDebugToggleEvent(source, arguments, DebugType.VERBOSE_AI, DebugType.VERBOSE_TIME, DebugType.VERBOSE_WEATHER)
                "weather" -> sendDebugWeatherEvent(source, arguments)
                else -> source.displayToMe("Did not understand debug command.")
            }
        }
    }

    private fun sendDebugToggleEvent(source: Thing, args: Args, vararg types: DebugType) {
        val toggleWords = args.hasAny(listOf("on", "off", "true", "false"))
        val toggledOn = if (toggleWords.isNotEmpty()) {
            toggleWords.contains("on") || toggleWords.contains("true")
        } else {
            !GameState.getDebugBoolean(types.first())
        }

        EventManager.postEvent(DebugToggleEvent(source, types.toList(), toggledOn))
    }

    private suspend fun sendDebugStatEvent(source: Thing, type: StatKind, args: Args) {
        val thing = parseThingsFromLocation(source, args.getGroup("on")).firstOrNull()?.thing ?: source
        val level = args.getNumber()

        if (level == null) {
            source.displayToMe("Could not find what number to set stat to: ${args.fullString}")
        } else {
            val statName = args.argsWithout(listOf("remove", args.args.first(), level.toString())).joinToString(" ")

            EventManager.postEvent(DebugStatEvent(thing, type, statName, level))
        }
    }

    private suspend fun sendDebugTagEvent(source: Thing, args: Args) {
        val thing = parseThingsFromLocation(source, args.getGroup("on")).firstOrNull()?.thing ?: source
        val tagName = args.argsWithout(listOf("remove", args.args.first())).joinToString(" ")
        val isAdding = !args.contains("remove")

        EventManager.postEvent(DebugTagEvent(thing, tagName, isAdding))
    }

    private suspend fun sendDebugWeatherEvent(source: Thing, arguments: Args) {
        val weather = arguments.argsWithout(listOf("weather")).joinToString(" ")
        if (weather.isBlank()) {
            source.displayToMe("Current weather is ${source.location.getLocation().weather.name}.")
            source.displayToMe("Weathers are ${WeatherManager.getAllWeather().joinToString { it.name }}")
        } else {
            EventManager.postEvent(DebugWeatherEvent(source, weather))
        }
    }

}

package system.debug

import core.GameState
import core.commands.Args
import core.commands.Command
import core.commands.parseTargets
import core.events.EventManager
import core.history.display
import core.history.displayYou
import core.target.Target
import status.stat.StatKind

class DebugCommand : Command() {

    override fun getAliases(): List<String> {
        return listOf("debug", "db")
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
    Debug stat <stat name> <desired level> on *<target> - Set a stat to the desired level.
    Debug prop <prop name> <desired level> on *<target> - Set a property to the desired level.
    Debug tag *<remove> <tag name> on *<target> - Add (or remove) a tag.
    Debug weather <weather name> - Set weather in current location to the given weather, if it exists.
        """
    }

    override fun getCategory(): List<String> {
        return listOf("Debugging")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("on"))

        if (args.isEmpty()) {
            sendDebugToggleEvent(source, DebugType.DEBUG_GROUP, arguments)
        } else {
            when (args.first()) {
                "list" -> EventManager.postEvent(DebugListEvent(source))
                "lvlreq" -> sendDebugToggleEvent(source, DebugType.LEVEL_REQ, arguments)
                "statchanges" -> sendDebugToggleEvent(source, DebugType.STAT_CHANGES, arguments)
                "random" -> sendDebugToggleEvent(source, DebugType.RANDOM_SUCCEED, arguments)
                "map" -> sendDebugToggleEvent(source, DebugType.MAP_SHOW_ALL_LOCATIONS, arguments)
                "clarity" -> sendDebugToggleEvent(source, DebugType.CLARITY, arguments)
                "displayupdates" -> sendDebugToggleEvent(source, DebugType.DISPLAY_UPDATES, arguments)
                "stat" -> sendDebugStatEvent(source, StatKind.LEVELED, arguments)
                "prop" -> sendDebugStatEvent(source, StatKind.PROP_VAL, arguments)
                "tag" -> sendDebugTagEvent(source, arguments)
                "weather" -> sendDebugWeatherEvent(source, arguments)
                else -> source.displayYou("Did not understand debug command.")
            }
        }
    }

    private fun sendDebugToggleEvent(source: Target, type: DebugType, args: Args) {
        val toggleWords = args.hasAny(listOf("on", "off", "true", "false"))
        val toggledOn = if (toggleWords.isNotEmpty()) {
            toggleWords.contains("on") || toggleWords.contains("true")
        } else {
            !GameState.getDebugBoolean(type)
        }

        EventManager.postEvent(DebugToggleEvent(source, type, toggledOn))
    }

    private fun sendDebugStatEvent(source: Target, type: StatKind, args: Args) {
        val target = parseTargets(source, args.getGroup("on")).firstOrNull()?.target ?: source
        val level = args.getNumber()

        if (level == null) {
            source.displayYou("Could not find what number to set stat to: ${args.fullString}")
        } else {
            val statName = args.argsWithout(listOf("remove", args.args.first(), level.toString())).joinToString(" ")

            EventManager.postEvent(DebugStatEvent(target, type, statName, level))
        }
    }

    private fun sendDebugTagEvent(source: Target, args: Args) {
        val target = parseTargets(source, args.getGroup("on")).firstOrNull()?.target ?: source
        val tagName = args.argsWithout(listOf("remove", args.args.first())).joinToString(" ")
        val isAdding = !args.contains("remove")

        EventManager.postEvent(DebugTagEvent(target, tagName, isAdding))
    }

    private fun sendDebugWeatherEvent(source: Target, arguments: Args) {
        val weather = arguments.argsWithout(listOf("weather")).joinToString(" ")
        EventManager.postEvent(DebugWeatherEvent(source, weather))
    }

}
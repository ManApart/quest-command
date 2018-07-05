package commands

import commands.parsing.LocationParsing
import events.MapEvent
import gameState.GameState
import gameState.Location
import processing.EventManager
import removeFirstItem

class MapCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Map", "m")
    }

    override fun getDescription(): String {
        return "Map:\n\tGet information on your current and other locations"
    }

    override fun getManual(): String {
        return "\n\tMap - Describe your current position." +
                "\n\tMap <location> - List locations at the given location (or given location)." +
                "\n\tMap here *<location> - List locations at your current location (or given location)." +
                "\n\tMap nearby *<location> - List locations near your current location (or given location)."
    }

    override fun execute(args: List<String>) {
        when{
            args.isEmpty() -> currentLocation()
            args[0] == "here" -> subLocations(removeFirstItem(args))
            args[0] == "nearby" -> surroundingLocations(removeFirstItem(args))
            else -> println("Unknown args: ${args.joinToString(" ")}. Did you mean 'map here' or 'map nearby'?")
        }
    }


    private fun currentLocation(){
        EventManager.postEvent(MapEvent(GameState.player.location, MapEvent.Type.INFO))
    }

    private fun subLocations(args: List<String>){
        val target = findLocation(args)
        if (target != GameState.world){
            EventManager.postEvent(MapEvent(target, MapEvent.Type.CHILDREN))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }

    }

    private fun surroundingLocations(args: List<String>){
        val target = findLocation(args)
        if (target != GameState.world){
            EventManager.postEvent(MapEvent(target, MapEvent.Type.SIBLINGS))
        } else {
            println("Could not find ${args.joinToString(" ")} on the map.")
        }
    }

    private fun findLocation(args: List<String>): Location {
        return if (args.isEmpty()) {
            GameState.player.location
        } else {
            LocationParsing.findLocation(GameState.player.location, args)
        }
    }
}
package commands

import events.MapEvent
import gameState.GameState
import gameState.Location
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
                "\n\tMap here *<location> - List locations at your current position (or given location)." +
                "\n\tMap nearby *<location> - List locations near your current position (or given location)."
    }

    override fun execute(args: List<String>) {
        when{
            args.isEmpty() -> currentLocation()
            args[0] == "here" -> subLocations(removeFirstItem(args))
            args[0] == "nearby" -> surroundingLocations(removeFirstItem(args))
        }
    }


    private fun currentLocation(){
        EventManager.postEvent(MapEvent(GameState.player.location, MapEvent.Type.INFO))
    }

    private fun subLocations(args: List<String>){
        EventManager.postEvent(MapEvent(findLocation(args), MapEvent.Type.CHILDREN))
    }

    private fun surroundingLocations(args: List<String>){
        EventManager.postEvent(MapEvent(findLocation(args), MapEvent.Type.SIBLINGS))
    }

    private fun findLocation(args: List<String>): Location {
        return if (args.isEmpty()) {
            GameState.player.location
        } else {
            GameState.world.findLocation(args)
        }
    }
}
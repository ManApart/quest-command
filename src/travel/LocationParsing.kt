package travel

import core.gameState.GameState
import core.gameState.Location

object LocationParsing {
    fun findLocation(source: Location, args: List<String>): Location {
        if (source.name.toLowerCase() == args.joinToString(" ").toLowerCase()) return source
        val found = findLocation(args)
        if (found == GameState.world){
            val args2 = (pathAsString(GameState.player.location) + " " + args.joinToString(" ")).split(" ")
            return findLocation(args2)
        }
        return found
    }

    private fun findLocation(args: List<String>) : Location {
        val found = GameState.world.findLocation(args)
        val argPath = args.joinToString(" ")
        val foundPath = pathAsString(found)

        return if (foundPath == argPath) {
            found
        } else {
            GameState.world
        }
    }

    private fun pathAsString(location: Location): String {
        val path = location.getPath()
        val trimmed = if (path.size > 1 && path[0].toLowerCase() == "world") path.subList(1, path.size) else path
        return trimmed.joinToString(" ").toLowerCase()
    }
}
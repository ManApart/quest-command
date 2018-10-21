package travel.jump

import core.commands.Command
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.Position
import system.EventManager
import travel.climb.ClimbJourney

class JumpCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Jump", "j")
    }

    override fun getDescription(): String {
        return "Jump:\n\tJump over obstacles or down to a lower area."

    }

    override fun getManual(): String {
        return "\n\tJump <obstacle> - Jump over an obstacle. X" +
                "\n\tJump - Jump down to the location below, possibly taking damage."
    }

    override fun getCategory(): List<String> {
        return listOf("Travel")
    }

    override fun execute(keyword: String, args: List<String>) {
        val loc = GameState.player.creature.location
        if (GameState.journey is ClimbJourney) {
            val location = GameState.player.creature.location
            val distance = (GameState.journey as ClimbJourney).getCurrentDistance()
            EventManager.postEvent(JumpEvent(source = location, destination = location, fallDistance = distance))
        } else {
            val found = loc.findChildLocation(Direction.BELOW).firstOrNull { it != loc }
                    ?: if (loc.position.isDirection(Direction.ABOVE, Position())) {
                        loc.getParent()
                    } else {
                        null
                    }
                    ?: loc.findSiblings(Direction.BELOW).firstOrNull { it != loc }
            if (found != null) {
                EventManager.postEvent(JumpEvent(source = GameState.player.creature.location, destination = found))
            } else {
                println("Couldn't find anything below to jump down to.")
            }
        }
    }

}
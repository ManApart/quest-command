package travel.climb

import core.commands.Command
import core.gameState.GameState
import core.history.display
import interact.ScopeManager
import system.EventManager

class ClimbCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Climb", "c", "Scale")
    }

    override fun getDescription(): String {
        return "Climb:\n\tClimb over obstacles"
    }

    override fun getManual(): String {
        return "\n\tClimb <target> - Climb the target" +
                "\n\tClimb <target> f - Keep climbing the target until you climbPath to the top or fall" +
                "\n\tClimb <number> - Climb the path with that number" +
                "\n\tClimb *up *down - Continue climbing (auto select first path in the correct direction)"
    }

    override fun getCategory(): List<String> {
        return listOf("Travel")
    }

    override fun execute(keyword: String, args: List<String>) {
        val adjustedArgs = args.toMutableList()
        val force = adjustedArgs.remove("f")
        val up = adjustedArgs.remove("up") || adjustedArgs.remove("u")
        val down = adjustedArgs.remove("down") || adjustedArgs.remove("d")

        val argsString = adjustedArgs.joinToString(" ")

        if (adjustedArgs.isEmpty() && !up && !down) {
            if (isClimbing()) {
                val step = getStep(GameState.player.climbJourney as ClimbJourney)
                climbStep(step, force)
            } else {
                display("What do you want to climb?")
            }
        } else {
            if (isClimbing()) {
                val journey = GameState.player.climbJourney as ClimbJourney
                if (adjustedArgs.isNotEmpty() && isStep(adjustedArgs[0])) {
                    val step = getStep(journey, adjustedArgs[0])
                    climbStep(step, force)
                } else if (up || down) {
                    climbTowardsDirection(up, journey, force)
                } else {
                    display("Unable to climb: ${args.joinToString(" ")}")
                }
            } else {
                if (ScopeManager.targetExists(argsString)) {
                    EventManager.postEvent(StartClimbingEvent(GameState.player.creature, ScopeManager.getTarget(argsString), force))
                } else {
                    display("Unable to climb: ${args.joinToString(" ")}")
                }
            }
        }
    }

    private fun climbTowardsDirection(upwards: Boolean, journey: ClimbJourney, force: Boolean) {
        if (upwards && journey.getCurrentSegment().top) {
            EventManager.postEvent(ClimbCompleteEvent(GameState.player.creature, journey.target, GameState.player.creature.location, journey.top))
        } else if (!upwards && journey.getCurrentSegment().bottom) {
            EventManager.postEvent(ClimbCompleteEvent(GameState.player.creature, journey.target, GameState.player.creature.location, journey.bottom))
        } else {
            val step = getStep(journey, upwards)
            climbStep(step, force)
        }
    }

    private fun isClimbing(): Boolean {
        return GameState.player.climbJourney != null && GameState.player.climbJourney is ClimbJourney
    }

    private fun isStep(word: String): Boolean {
        return word.toIntOrNull() != null
    }

    private fun getStep(journey: ClimbJourney): Int {
        return journey.getNextSegments().firstOrNull()?.id ?: 0
    }

    private fun getStep(journey: ClimbJourney, upwards: Boolean): Int {
        return journey.getNextSegments(upwards).firstOrNull()?.id ?: 0
    }

    private fun getStep(journey: ClimbJourney, choiceWord: String): Int {
        val choice = choiceWord.toInt() - 1
        val segments = journey.getHigherSegments().toMutableList()
        segments.addAll(journey.getLowerSegments())
        return if (choice < segments.size) {
            segments[choice].id
        } else {
            0
        }
    }

    private fun climbStep(step: Int, force: Boolean) {
        if (step != 0) {
            EventManager.postEvent(ClimbJourneyEvent(step, force))
        } else {
            display("Couldn't find the next place to climb to!")
        }
    }

}
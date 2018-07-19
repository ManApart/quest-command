package travel.climb

import core.commands.Command
import core.gameState.GameState
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
                "\n\tClimb <target> f - Keep climbing the target until you climbPath to the top or fall. X" +
                "\n\tClimb <number> - Climb the path with that number" +
                "\n\tClimb *up *down - Continue climbing (auto select first path in the correct direction)"
    }

    override fun getCategory(): List<String> {
        return listOf("Travel")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")
        if (args.isEmpty()) {
            if (isClimbing()) {
                val step = getStep(GameState.journey as ClimbJourney)
                climbStep(step)
            } else {
                println("What do you want to climb?")
            }
        } else {
            if (isClimbing() && (isStep(args[0]) || isDirection(args[0]))) {
                val journey = GameState.journey as ClimbJourney
                if (isDirection(args[0])) {
                    val step = getStep(journey, getDirection(args[0]))
                    climbStep(step)
                } else if (isStep(args[0])) {
                    val step = getStep(journey, args[0])
                    climbStep(step)
                }
            } else if (ScopeManager.targetExists(argsString)) {
                EventManager.postEvent(ClimbStartEvent(GameState.player, ScopeManager.getTarget(argsString)))
            } else {
                println("Unable to climbPath: ${args.joinToString(" ")}")
            }
        }
    }

    private fun isClimbing(): Boolean {
        return GameState.journey != null && GameState.journey is ClimbJourney
    }

    private fun isDirection(word: String): Boolean {
        return listOf("up", "u", "down", "d").contains(word.toLowerCase())
    }

    private fun isStep(word: String): Boolean {
        return word.toIntOrNull() != null
    }

    private fun getDirection(word: String): Boolean {
        val cleaned = word.toLowerCase()
        if (cleaned == "up" || cleaned == "u") {
            return true
        }
        return false
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
        return if (choice < segments.size){
            segments[choice].id
        } else {
            0
        }
    }

    private fun climbStep(step: Int) {
        if (step != 0) {
            EventManager.postEvent(ClimbJourneyEvent(step))
        } else {
            println("Couldn't find the next place to climb to!")
        }
    }

}
package explore

import core.gameState.GameState
import core.history.display
import core.utility.wrapNonEmpty
import travel.climb.ClimbJourney

object ClimbLook {

    fun describeClimbJourney() {
        val journey = GameState.player.climbJourney as ClimbJourney
        val distance = getDistance(journey).wrapNonEmpty("", " ")
        val abovePaths = getAbovePaths(journey).wrapNonEmpty("", " ")
        val belowPaths = getBelowPaths(journey)

        display("$distance$abovePaths$belowPaths")
    }

    private fun getDistance(journey: ClimbJourney): String {
        return if (journey.getCurrentDistance() > 1) {
            "You are ${journey.getCurrentDistance()}ft up."
        } else {
            ""
        }
    }

    private fun getAbovePaths(journey: ClimbJourney): String {
        return if (journey.getHigherSegments().size > 1) {
            var i = 0
            "Above you are path choices " + journey.getHigherSegments().joinToString(", ") {
                i++
                "$i"
            } + "."
        } else if (journey.getHigherSegments().size == 1) {
            ""
        } else {
            if (journey.getCurrentSegment().top) {
                "You are at the top of ${journey.target.name}."
            } else {
                "Above you is nothing to attemptClimb on."
            }
        }
    }

    private fun getBelowPaths(journey: ClimbJourney): String {
        return if (journey.getLowerSegments().size > 1) {
            var i = journey.getHigherSegments().size
            "Below you are path choices " + journey.getLowerSegments().joinToString(",") {
                i++
                "$i"
            } + "."
        } else if (journey.getLowerSegments().size == 1) {
            ""
        } else {
            if (journey.getCurrentSegment().bottom) {
                "You are at the bottom of ${journey.target.name}."
            } else {
                "Below you is nothing to attemptClimb on."
            }
        }
    }
}
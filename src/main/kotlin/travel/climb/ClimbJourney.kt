package travel.climb

import core.gameState.Target
import core.gameState.climb.ClimbPath
import core.gameState.climb.ClimbSegment
import core.gameState.location.LocationNode
import core.history.display

class ClimbJourney(val target: Target, origin: LocationNode, destination: LocationNode, upwards: Boolean, private val path: ClimbPath) {
    private var step = 0
    private var lastDirectionWasUp = upwards
    val top = if (upwards) {
        destination
    } else {
        origin
    }
    val bottom = if (upwards) {
        origin
    } else {
        destination
    }

    private fun hasSegment(step: Int): Boolean {
        return path.segments.firstOrNull { it.id == step } != null
    }

    fun getCurrentSegment(): ClimbSegment {
        return getSegment(step)
    }

    fun getSegment(id: Int): ClimbSegment {
        return path.segments.first { it.id == id }
    }

    /**
     * Returns the next segment based on the previous direction taken
     */
    fun getNextSegments(): List<ClimbSegment> {
        return getNextSegments(lastDirectionWasUp)
    }

    fun getNextSegments(upwards: Boolean): List<ClimbSegment> {
        return if (upwards) {
            getHigherSegments(step)
        } else {
            getLowerSegments(step)
        }
    }

    fun getPreviousSegments(upwards: Boolean): List<ClimbSegment> {
        return if (upwards) {
            getLowerSegments(step)
        } else {
            getHigherSegments(step)
        }
    }

    fun getLowerSegments(): List<ClimbSegment> {
        return getLowerSegments(step)
    }

    fun getHigherSegments(): List<ClimbSegment> {
        return getHigherSegments(step)
    }

    private fun getLowerSegments(currentStep: Int): List<ClimbSegment> {
        return path.segments.filter { it.higherSegments.contains(currentStep) }
    }

    private fun getHigherSegments(currentStep: Int): List<ClimbSegment> {
        return if (hasSegment(currentStep)) {
            getSegment(currentStep).higherSegments.map { getSegment(it) }
        } else {
            listOf()
        }
    }

    fun isPathEnd(desiredStep: Int): Boolean {
        if (step == 0) {
            return false
        }
        val upwards = getDirection(desiredStep)
        return if (upwards) {
            getSegment(desiredStep).top
        } else {
            getSegment(desiredStep).bottom
        }
    }

    fun getDirection(desiredStep: Int): Boolean {
        return if (step == 0) {
            (desiredStep == path.getBottom())
        } else {
            getCurrentSegment().higherSegments.contains(desiredStep)
        }
    }

    fun getDistanceTo(step: Int) : Int {
        return Math.abs(getDistance(step) - getCurrentDistance())
    }

    fun getCurrentDistance(): Int {
        return if (step == 0) {
            if (lastDirectionWasUp) {
                0
            } else {
                getTotalDistance()
            }
        } else {
            getDistance(step)
        }
    }

    private fun getDistance(currentStep: Int): Int {
        var distance = getSegment(currentStep).distance
        if (getLowerSegments(currentStep).isNotEmpty()) {
            distance += getDistance(getLowerSegments(currentStep).first().id)
        }
        return distance
    }

    private fun getTotalDistance() : Int {
        return getDistance(path.getTop())
    }

    fun getDestination(desiredStep: Int): LocationNode {
        return if (desiredStep == path.getTop()) {
            top
        } else {
            bottom
        }
    }

    fun advance(desiredStep: Int) {
        if (hasSegment(desiredStep)) {
            lastDirectionWasUp = getDirection(desiredStep)
            step = desiredStep

        } else {
            display("Couldn't advance journey to $desiredStep. This shouldn't happen!")
        }
    }
}
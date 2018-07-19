package core.gameState.climb

import core.gameState.Direction

class ClimbPath(val name: String, val segments: List<ClimbSegment>){
    fun getStart(direction: Direction) : Int {
        return if (direction == Direction.BELOW){
            getTop()
        } else {
            getBottom()
        }
    }

    fun getBottom() : Int {
        return segments.first { it.bottom }.id
    }

    fun getTop() : Int {
        return segments.first { it.top }.id
    }
}
package core.gameState.climb

class ClimbPath(val name: String, val segments: List<ClimbSegment>){
    fun getStart(upwards: Boolean) : Int {
        return if (upwards){
            getBottom()
        } else {
            getTop()
        }
    }

    fun getBottom() : Int {
        return segments.first { it.bottom }.id
    }

    fun getTop() : Int {
        return segments.first { it.top }.id
    }
}
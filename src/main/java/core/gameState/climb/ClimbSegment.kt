package core.gameState.climb

class ClimbSegment(val id: Int, val level: Int, val distance: Int, val bottom: Boolean = false, val top: Boolean = false, val higherSegments: List<Int> = listOf())
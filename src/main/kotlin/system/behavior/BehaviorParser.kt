package system.behavior

import core.gameState.behavior.BehaviorBase

interface BehaviorParser {
    fun loadBehaviors(): List<BehaviorBase>
}
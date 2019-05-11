package system

import core.gameState.behavior.BehaviorBase
import system.behavior.BehaviorParser

class BehaviorFakeParser(private val behaviors: List<BehaviorBase> = listOf()) : BehaviorParser {
    override fun loadBehaviors(): List<BehaviorBase> {
        return behaviors
    }
}
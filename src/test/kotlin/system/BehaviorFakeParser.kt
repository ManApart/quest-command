package system

import core.ai.behavior.BehaviorBase
import core.ai.behavior.BehaviorParser

class BehaviorFakeParser(private val behaviors: List<BehaviorBase> = listOf()) : BehaviorParser {
    override fun loadBehaviors(): List<BehaviorBase> {
        return behaviors
    }
}
package system

import core.ai.behavior.Behavior
import core.ai.behavior.BehaviorParser

class BehaviorFakeParser(private val behaviors: List<Behavior<*>> = listOf()) : BehaviorParser {
    override fun loadBehaviors(): List<Behavior<*>> {
        return behaviors
    }
}
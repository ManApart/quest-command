package system

import core.gameState.behavior.BehaviorBase
import core.gameState.body.BodyPart
import core.gameState.body.ProtoBody
import system.behavior.BehaviorParser
import system.body.BodyParser

class BehaviorFakeParser(private val behaviors: List<BehaviorBase> = listOf()) : BehaviorParser {
    override fun loadBehaviors(): List<BehaviorBase> {
        return behaviors
    }
}
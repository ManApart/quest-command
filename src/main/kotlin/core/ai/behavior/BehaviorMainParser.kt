package core.ai.behavior

import resources.behaviors.BaseBehaviors

class BehaviorMainParser : BehaviorParser {
    override fun loadBehaviors(): List<Behavior<*>> {
        return BaseBehaviors().values
    }
}
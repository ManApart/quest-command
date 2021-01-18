package core.ai.behavior

import resources.behaviors.behaviorsList

class BehaviorMainParser : BehaviorParser {
    override fun loadBehaviors(): List<Behavior<*>> {
        return behaviorsList
    }
}
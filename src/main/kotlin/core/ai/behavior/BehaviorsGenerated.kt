package core.ai.behavior

class BehaviorsGenerated : BehaviorsCollection {
    override val values = listOf<BehaviorResource>(resources.behaviors.BaseBehaviors()).flatMap { it.values }
}
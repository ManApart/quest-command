package core.ai.behavior

class BehaviorsGenerated : BehaviorsCollection {
    override val values = listOf<BehaviorResource>(resources.behaviors.CommonBehaviors()).flatMap { it.values }
}
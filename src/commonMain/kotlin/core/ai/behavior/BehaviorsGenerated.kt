package core.ai.behavior

class BehaviorsGenerated : BehaviorsCollection {
    override val values by lazy { listOf<BehaviorResource>(resources.behaviors.CommonBehaviors()).flatMap { it.values }}
}
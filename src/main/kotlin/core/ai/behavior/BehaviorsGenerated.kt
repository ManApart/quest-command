package core.ai.behavior

class BehaviorsGenerated : BehaviorsCollection {
    override val values: List<Behavior<*>> = listOf(resources.behaviors.BaseBehaviors()).flatMap { it.values }
}
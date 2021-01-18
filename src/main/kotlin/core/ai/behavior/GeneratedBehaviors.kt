package core.ai.behavior

class GeneratedBehaviors : BehaviorsCollection {
    override val values: List<Behavior<*>> = listOf(resources.behaviors.BaseBehaviors()).flatMap { it.values }
}
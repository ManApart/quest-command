package core.ai.behavior
import core.ai.behavior.Behavior

class BehaviorsGenerated : BehaviorsCollection {
    override val values: List<Behavior<*>> = listOf(resources.behaviors.BaseBehaviors()).flatMap { it.values }
}
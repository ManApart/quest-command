package core.ai.behavior
import core.ai.behavior.Behavior

interface BehaviorsCollection {
    val values: List<Behavior<*>>
}
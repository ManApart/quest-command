package core.ai.behavior

interface BehaviorParser {
    fun loadBehaviors(): List<Behavior<*>>
}
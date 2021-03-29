package core.ai.action.dsl

class AIActionsGenerated : AIActionsCollection {
    override val values = listOf<AIActionResource>().flatMap { it.values }
}
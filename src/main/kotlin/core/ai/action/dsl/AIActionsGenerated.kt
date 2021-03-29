package core.ai.action.dsl

class AIActionsGenerated : AIActionsCollection {
    override val values = listOf<AIActionResource>(resources.ai.actions.CommonActions()).flatMap { it.values }
}
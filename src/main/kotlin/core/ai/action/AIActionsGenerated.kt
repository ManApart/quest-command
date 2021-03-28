package core.ai.action

class AIActionsGenerated : AIActionsCollection {
    override val values = listOf<AIActionResource>().flatMap { it.values }
}
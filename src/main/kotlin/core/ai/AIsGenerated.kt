package core.ai

class AIsGenerated : AIsCollection {
    override val values = listOf<AIResource>(resources.ai.CommonAI(), resources.ai.actions.CommonActions()).flatMap { it.values }
}
package core.ai.dsl

class AIsGenerated : AIsCollection {
    override val values = listOf<AIResource>(resources.ai.CommonAI()).flatMap { it.values }
}
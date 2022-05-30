package core.ai.dsl

class AIsGenerated : AIsCollection {
    override val values by lazy { listOf<AIResource>(resources.ai.CommonAI()).flatMap { it.values }}
}
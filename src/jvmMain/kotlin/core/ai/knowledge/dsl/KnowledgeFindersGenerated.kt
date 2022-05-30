package core.ai.knowledge.dsl

class KnowledgeFindersGenerated : KnowledgeFindersCollection {
    override val values by lazy { listOf<KnowledgeFinderResource>(resources.ai.CommonKnowledgeFinders()).flatMap { it.values }}
}
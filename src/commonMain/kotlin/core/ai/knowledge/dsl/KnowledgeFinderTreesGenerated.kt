package core.ai.knowledge.dsl

class KnowledgeFinderTreesGenerated : KnowledgeFinderTreesCollection {
    override val values by lazy { listOf<KnowledgeFinderTreeResource>(resources.ai.CommonKnowledgeFinders()).flatMap { it.values }}
}
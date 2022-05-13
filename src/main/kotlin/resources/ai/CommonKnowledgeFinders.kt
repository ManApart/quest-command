package resources.ai

import core.ai.knowledge.KnowledgeFinder
import core.ai.knowledge.dsl.KnowledgeFinderResource

class CommonKnowledgeFinders: KnowledgeFinderResource  {
    override val values = listOf<KnowledgeFinder>()
}
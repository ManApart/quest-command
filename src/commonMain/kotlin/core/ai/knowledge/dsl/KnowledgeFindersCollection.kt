package core.ai.knowledge.dsl
import core.ai.knowledge.KnowledgeFinder

interface KnowledgeFindersCollection {
    val values: List<KnowledgeFinder>
}
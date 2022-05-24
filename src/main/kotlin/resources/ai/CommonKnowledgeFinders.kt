package resources.ai

import core.ai.knowledge.*
import core.ai.knowledge.dsl.KnowledgeFinderResource
import core.ai.knowledge.dsl.knowledgeFinders

class CommonKnowledgeFinders : KnowledgeFinderResource {
    override val values = knowledgeFinders {
        factFull(::getPersonalFact)
        relationshipFull(::getPersonalRelationship)

        kind("Is") {
            source({ it.thing != null }) {
                source({ it.propertyTag == "Rich" }) {
                    fact { _, _, _ -> Pair(0, 0) }
                }
                relatesTo({ it.location != null }) {
                    relationship { _, _, _, _ -> Pair(0, 0) }
                }
            }
        }
    }

    private fun getPersonalFact(mind: Mind, source: Subject, kind: String): Fact {
        return mind.personalFacts[kind]
            ?.filter { it.source == source }
            ?.average()
            ?: Fact(source, kind, 0, 0)
    }

    private fun getPersonalRelationship(mind: Mind, source: Subject, kind: String, relatesTo: Subject): Relationship {
        return mind.personalRelationships[kind]
            ?.filter { it.source == source && it.relatesTo == relatesTo }
            ?.average()
            ?: Relationship(source, kind, relatesTo, 0, 0)
    }
}
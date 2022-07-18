package resources.ai

import core.ai.knowledge.*
import core.ai.knowledge.dsl.KnowledgeFinderResource
import core.ai.knowledge.dsl.Opinion
import core.ai.knowledge.dsl.knowledgeFinders

class CommonKnowledgeFinders : KnowledgeFinderResource {
    override val values = knowledgeFinders {
        factFull(::getPersonalFact)
        listFactFull(::getPersonalListFact)
        relationshipFull(::getPersonalRelationship)

        kind("Is") {
            source({ it.thing != null }) {
                source({ it.propertyTag == "Rich" }) {
                    fact { _, _, _ -> Opinion() }
                }

                relatesTo({ it.thing != null }) {
                    kind("likes"){
                        relationship { mind: Mind, source: Subject, kind: String, relatesTo: Subject -> Opinion(100, 0) }
                    }
                    relationship { mind: Mind, source: Subject, kind: String, relatesTo: Subject -> Opinion() }
                }

                relatesTo({ it.location != null }) {
                    relationship { _, _, _, _ -> Opinion() }
                }
            }

            source({ it.location != null }) {
            }
        }
    }

    private fun getPersonalFact(mind: Mind, source: Subject, kind: String): Fact {
        return mind.memory.getFact(source, kind)
            ?: Fact(SimpleSubject(source), kind, 0, 0)
    }

    private fun getPersonalListFact(mind: Mind, kind: String): ListFact {
        return mind.memory.getListFact(kind)
            ?: ListFact(kind, emptyList())
    }

    private fun getPersonalRelationship(mind: Mind, source: Subject, kind: String, relatesTo: Subject): Relationship {
        return mind.memory.getRelationship(source, kind, relatesTo)
            ?: Relationship(SimpleSubject(source), kind, SimpleSubject(relatesTo), 0, 0)
    }
}
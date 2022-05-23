package resources.ai

import core.ai.knowledge.*
import core.ai.knowledge.dsl.KnowledgeFinderResource
import core.ai.knowledge.dsl.knowledgeFinders

class CommonKnowledgeFinders : KnowledgeFinderResource {
    override val values = knowledgeFinders {
        //Get any fact about any kind from the relevant mind
        factFull { mind, source, kind -> mind.personalFacts[kind]?.filter { it.source == source }?.sum(source, kind) ?: Fact(source, kind, 0, 0) }

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
}
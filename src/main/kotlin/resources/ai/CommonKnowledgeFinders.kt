package resources.ai

import core.ai.knowledge.Fact
import core.ai.knowledge.Mind
import core.ai.knowledge.Subject
import core.ai.knowledge.dsl.KnowledgeFinderResource
import core.ai.knowledge.dsl.knowledgeFinders
import core.ai.knowledge.sum

class CommonKnowledgeFinders : KnowledgeFinderResource {
    override val values = knowledgeFinders {
        //Get any fact about any kind from the relevant mind
        fact {
            find { mind, source, kind -> mind.personalFacts[kind]?.filter { it.source == source }?.sum(source, kind) ?: Fact(source, kind, 0, 0) }
        }
        
        kind("Is") {

            fact {
                source { it.thing != null && it.propertyTag == "Rich" }
                find { _: Mind, source: Subject, kind: String -> Fact(source, kind, 0, 0) }
            }
        }
    }
}

//TODO - reorder so relevance can be nested
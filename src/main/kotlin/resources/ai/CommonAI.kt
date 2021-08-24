package resources.ai

import core.ai.dsl.AIResource
import core.ai.dsl.aiBuilder

class CommonAI : AIResource {
    override val values = aiBuilder {
        ai("Cowardly Predator") {
            additional = listOf("Nothing", "Rat Attack")
        }
        ai("Commoner") {
            additional = listOf("Nothing")
        }
    }
}

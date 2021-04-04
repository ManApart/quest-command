package resources.ai

import core.ai.dsl.AIResource
import core.ai.dsl.aiBuilder

class CommonAI : AIResource {
    override val values = aiBuilder {
        ai("name") {
            inherits = listOf("parent")
            additional = listOf("action")
            exclude = listOf("action")
        }
        ai("Cowardly Predator") {
            additional = listOf("Nothing", "Rat Attack")
        }
        ai("Commoner") {
            additional = listOf("Nothing")
        }
    }.build()
}

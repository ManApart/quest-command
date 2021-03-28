package core.ai

import core.target.Target
import core.utility.NameSearchableList
import core.DependencyInjector
import core.ai.behavior.BehaviorsCollection
import core.utility.toNameSearchableList

object AIManager {
    private var parser = DependencyInjector.getImplementation(AIsCollection::class.java)
    private var AIs = parser.values.toNameSearchableList()
    private val defaultAI = AIBase("NONE")
    private val playerControlledAI = AIBase(PLAYER_CONTROLLED_ID)

    fun reset() {
        parser = DependencyInjector.getImplementation(AIsCollection::class.java)
        AIs = parser.values.toNameSearchableList()
    }

    fun getAI(name: String?, creature: Target): AI {
        return when {
            name == PLAYER_CONTROLLED_ID -> playerControlledAI.createPlayerControlled(creature)
            name != null && AIs.exists(name) -> AIs.get(name).createConditional(creature)
            else -> defaultAI.createConditional(creature)
        }
    }

}
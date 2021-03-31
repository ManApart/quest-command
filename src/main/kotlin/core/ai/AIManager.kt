package core.ai

import core.target.Target
import core.DependencyInjector
import core.ai.action.AIAction
import core.ai.action.dsl.AIActionsCollection
import core.ai.dsl.AIsCollection
import core.utility.toNameSearchableList

object AIManager {
    private var aIsCollection = DependencyInjector.getImplementation(AIsCollection::class.java)
    private var actionsCollection = DependencyInjector.getImplementation(AIActionsCollection::class.java)

    private var AIs = aIsCollection.values.toNameSearchableList()
    private var actions = actionsCollection.values.toNameSearchableList()

    private val defaultAI = AIBase("NONE")
    private val playerControlledAI = AIBase(PLAYER_CONTROLLED_ID)

    fun reset() {
        aIsCollection = DependencyInjector.getImplementation(AIsCollection::class.java)
        actionsCollection = DependencyInjector.getImplementation(AIActionsCollection::class.java)

        AIs = aIsCollection.values.toNameSearchableList()
        actions = actionsCollection.values.toNameSearchableList()
    }

    fun getAI(name: String?, creature: Target): AI {
        return when {
            name == PLAYER_CONTROLLED_ID -> playerControlledAI.createPlayerControlled(creature)
            name != null && AIs.exists(name) -> AIs.get(name).createConditional(creature)
            else -> defaultAI.createConditional(creature)
        }
    }

    fun getAIAction(name: String): AIAction {
        return actions.get(name)
    }

}
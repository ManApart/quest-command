package core.ai

import core.DependencyInjector
import core.ai.action.AIAction
import core.ai.action.dsl.AIActionsCollection
import core.ai.dsl.AIsCollection
import core.utility.toNameSearchableList

object AIManager {
    private var aIsCollection = DependencyInjector.getImplementation(AIsCollection::class)
    private var actionsCollection = DependencyInjector.getImplementation(AIActionsCollection::class)

    private var AIs = aIsCollection.values.toNameSearchableList()
    private var actions = actionsCollection.values.toNameSearchableList()
    private val defaultAI = AIBase("NONE")
    private val playerControlledAI = AIBase(PLAYER_CONTROLLED_ID)

    fun reset() {
        aIsCollection = DependencyInjector.getImplementation(AIsCollection::class)
        actionsCollection = DependencyInjector.getImplementation(AIActionsCollection::class)

        AIs = aIsCollection.values.toNameSearchableList()
        actions = actionsCollection.values.toNameSearchableList()
    }

    fun getAI(name: String?): AI {
        return when {
            name == PLAYER_CONTROLLED_ID -> playerControlledAI.createPlayerControlled()
            name != null && AIs.exists(name) -> AIs.get(name).createConditional()
            else -> defaultAI.createConditional()
        }
    }

    fun getAIAction(name: String): AIAction {
        return actions.get(name)
    }

}
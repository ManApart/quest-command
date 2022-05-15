package core.ai

import core.DependencyInjector
import core.ai.action.AIAction
import core.ai.action.dsl.AIActionsCollection
import core.ai.dsl.AIsCollection
import core.startupLog
import core.utility.NameSearchableList
import core.utility.toNameSearchableList

object AIManager {
    private var AIs = loadAIs()
    private var actions = loadActions()
    private val defaultAI = AIBase("NONE")
    private val playerControlledAI = AIBase(PLAYER_CONTROLLED_ID)

    private fun loadAIs(): NameSearchableList<AIBase> {
        startupLog("Loading AI Bases.")
        return DependencyInjector.getImplementation(AIsCollection::class).values.toNameSearchableList()
    }

    private fun loadActions(): NameSearchableList<AIAction> {
        startupLog("Loading AI Actions.")
        return DependencyInjector.getImplementation(AIActionsCollection::class).values.toNameSearchableList()
    }

    fun reset() {
        AIs = loadAIs()
        actions = loadActions()
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
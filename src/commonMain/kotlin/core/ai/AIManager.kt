package core.ai

import core.DependencyInjector
import core.ai.action.AIAction
import core.ai.action.dsl.AIActionsCollection
import core.startupLog
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList

object AIManager {
    private var actions by lazyM { loadActions() }

    private fun loadActions(): NameSearchableList<AIAction> {
        startupLog("Loading AI Actions.")
        return DependencyInjector.getImplementation(AIActionsCollection::class).values.toNameSearchableList()
    }

    fun reset() {
        actions = loadActions()
    }

    fun getAIAction(name: String): AIAction {
        return actions.get(name)
    }

    fun getAIActions(): List<AIAction> {
        return actions.toList()
    }

}
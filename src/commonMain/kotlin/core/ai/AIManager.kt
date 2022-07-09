package core.ai

import core.DependencyInjector
import core.ai.action.AIAction
import core.ai.action.AIActionTree
import core.ai.action.dsl.AIActionsCollection
import core.startupLog
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList

object AIManager {
    private var actions by lazyM { loadActions() }

    private fun loadActions(): List<AIActionTree> {
        startupLog("Loading AI Actions.")
        return DependencyInjector.getImplementation(AIActionsCollection::class).values
    }

    fun reset() {
        actions = loadActions()
    }

    fun getAIActions(): List<AIActionTree> {
        return actions.toList()
    }

}
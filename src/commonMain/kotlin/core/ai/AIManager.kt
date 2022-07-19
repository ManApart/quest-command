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
    var actions by lazyM { loadActions() }
        private set

    private fun loadActions(): List<AIActionTree> {
        startupLog("Loading AI Actions.")
        return DependencyInjector.getImplementation(AIActionsCollection::class).values
    }

    fun reset() {
        actions = loadActions()
    }

}
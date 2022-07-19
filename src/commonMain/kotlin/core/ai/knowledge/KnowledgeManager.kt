package core.ai.knowledge

import core.DependencyInjector
import core.ai.knowledge.dsl.KnowledgeFindersCollection
import core.startupLog
import core.utility.lazyM

object KnowledgeManager {
    var knowledgeFinders by lazyM { loadFinders() }
        private set

    private fun loadFinders(): List<KnowledgeFinderTree> {
        startupLog("Loading Knowledge.")
        return DependencyInjector.getImplementation(KnowledgeFindersCollection::class).values
    }

    fun reset() {
        knowledgeFinders = loadFinders()
    }


}
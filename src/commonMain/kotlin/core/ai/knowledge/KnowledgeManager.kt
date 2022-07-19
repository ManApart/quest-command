package core.ai.knowledge

import core.DependencyInjector
import core.ai.knowledge.dsl.KnowledgeFinderTreesCollection
import core.startupLog
import core.utility.lazyM

object KnowledgeManager {
    var knowledgeFinders by lazyM { loadFinders() }
        private set

    private fun loadFinders(): List<KnowledgeFinderTree> {
        startupLog("Loading Knowledge.")
        return DependencyInjector.getImplementation(KnowledgeFinderTreesCollection::class).values
    }

    fun reset() {
        knowledgeFinders = loadFinders()
    }


}
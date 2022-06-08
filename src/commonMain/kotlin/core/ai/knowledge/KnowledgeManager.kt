package core.ai.knowledge

import core.DependencyInjector
import core.ai.knowledge.dsl.KnowledgeFindersCollection
import core.startupLog
import core.utility.lazyM

object KnowledgeManager {
    private var knowledgeFinders by lazyM { loadFinders() }
    var factFinders by lazyM { loadFactFinders() }
    var listFactFinders by lazyM { loadListFactFinders() }
    var relationshipFinders by lazyM { loadRelationshipFinders() }

    private fun loadFinders(): List<KnowledgeFinder> {
        startupLog("Loading Knowledge.")
        return DependencyInjector.getImplementation(KnowledgeFindersCollection::class).values
    }

    private fun loadFactFinders() = knowledgeFinders.mapNotNull { it.factFinder }
    private fun loadListFactFinders() = knowledgeFinders.mapNotNull { it.listFactFinder }
    private fun loadRelationshipFinders() = knowledgeFinders.mapNotNull { it.relationshipFinder }


    fun reset() {
        knowledgeFinders = loadFinders()
        factFinders = loadFactFinders()
        listFactFinders = loadListFactFinders()
        relationshipFinders = loadRelationshipFinders()
    }


}
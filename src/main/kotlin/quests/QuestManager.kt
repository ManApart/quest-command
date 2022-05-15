package quests

import core.DependencyInjector
import core.startupLog
import core.utility.NameSearchableList

object QuestManager {
    private var storyEvents = loadStoryEvents()
    var quests = parseQuests(storyEvents)

    private fun loadStoryEvents(): List<StoryEvent> {
        startupLog("Loading Story Events.")
        return DependencyInjector.getImplementation(StoryEventsCollection::class).values.map { it.copy() }
    }

    fun getActiveQuests(): NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active })
    }

    fun getAllPlayerQuests(): NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active || it.complete })
    }

    fun reset() {
        storyEvents = loadStoryEvents()
        quests = parseQuests(storyEvents)
    }

    private fun parseQuests(events: List<StoryEvent>): NameSearchableList<Quest> {
        startupLog("Parsing Quests.")
        val quests = mutableMapOf<String, Quest>()

        events.forEach { event ->
            if (!quests.containsKey(event.questName)) {
                quests[event.questName] = Quest(event.questName)
            }
            quests[event.questName]?.addEvent(event)
        }

        quests.values.forEach {
            it.initialize()
        }

        return NameSearchableList(quests.values.toList())
    }


}
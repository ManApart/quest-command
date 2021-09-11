package quests

import core.DependencyInjector
import core.utility.NameSearchableList

object QuestManager {
    var storyEvents = DependencyInjector.getImplementation(StoryEventsCollection::class).values.map { it.copy() }
    var quests = parseQuests(storyEvents)

    fun getActiveQuests() : NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active})
    }

    fun getAllPlayerQuests() : NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active || it.complete})
    }

    fun reset(){
        storyEvents = DependencyInjector.getImplementation(StoryEventsCollection::class).values.map { it.copy() }
        quests = parseQuests(storyEvents)
    }

    private fun parseQuests(events: List<StoryEvent>): NameSearchableList<Quest> {
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
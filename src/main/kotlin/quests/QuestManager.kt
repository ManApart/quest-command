package quests

import core.utility.NameSearchableList
import core.DependencyInjector

object QuestManager {
    private var parser = DependencyInjector.getImplementation(StoryEvent2sCollection::class.java)
    var quests = parseQuests(parser.values)

    fun getActiveQuests() : NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active})
    }

    fun getAllPlayerQuests() : NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active || it.complete})
    }

    fun reset(){
        parser = DependencyInjector.getImplementation(StoryEvent2sCollection::class.java)
        quests = parseQuests(parser.values)
    }

    //TODO - instead of this make resource files deliver full quests
    private fun parseQuests(events: List<StoryEvent2>): NameSearchableList<Quest> {
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
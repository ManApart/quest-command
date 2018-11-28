package core.gameState.quests

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.NameSearchableList

object QuestManager {
    val quests = loadQuests()

    private fun loadQuests(): NameSearchableList<Quest> {
        val events = loadStoryEvents()
        val quests = mutableMapOf<String, Quest>()

        events.forEach {event ->
            if (!quests.containsKey(event.questName)){
                quests[event.questName] = Quest(event.questName, event)
            }
            quests[event.questName]?.addEvent(event)
        }

        quests.values.forEach{
            it.calculateActiveEvent()
        }

        return NameSearchableList(quests.values.toList())
    }

    private fun loadStoryEvents(): List<StoryEvent> {
        val json = this::class.java.getResourceAsStream("/data/StoryEvents.json")
        return jacksonObjectMapper().readValue(json)
    }

    fun getActiveQuests() : NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active})
    }
}
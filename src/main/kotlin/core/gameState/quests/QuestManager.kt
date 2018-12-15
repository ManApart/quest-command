package core.gameState.quests

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import core.utility.JsonDirectoryParser
import core.utility.NameSearchableList

object QuestManager {
    var quests = loadQuests()

    private fun loadQuests(): NameSearchableList<Quest> {
        val events = JsonDirectoryParser.parseDirectory("/data/generated/content/story-events", ::parseFile)
        val quests = mutableMapOf<String, Quest>()

        events.forEach {event ->
            if (!quests.containsKey(event.questName)){
                quests[event.questName] = Quest(event.questName)
            }
            quests[event.questName]?.addEvent(event)
        }

        quests.values.forEach{
            it.initialize()
        }

        return NameSearchableList(quests.values.toList())
    }

    private fun parseFile(path: String): List<StoryEvent> = jacksonObjectMapper().readValue(this::class.java.getResourceAsStream(path))

    fun getActiveQuests() : NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active})
    }

    fun getAllPlayerQuests() : NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active || it.complete})
    }

    fun reset(){
        quests = loadQuests()
    }

}
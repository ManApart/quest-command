package validation

import quests.Quest
import quests.QuestManager
import kotlin.test.Test
import kotlin.test.assertEquals

class QuestValidator {

    private val quests = QuestManager.quests
    private val storyEvents = QuestManager.storyEvents

    @Test
    fun validate() {
        assertEquals(0,noDuplicateStages())
    }

    private fun noDuplicateStages(): Int {
        return quests.sumOf { noDuplicateStages(it) }
    }

    private fun noDuplicateStages(quest: Quest): Int {
        val eventCount = storyEvents.filter { it.questName == quest.name }.size
        if (eventCount != quest.getAllEvents().size) {
            println("WARN: Quest ${quest.name} possibly has duplicate stages.")
            return 1
        }
        return 0
    }

}

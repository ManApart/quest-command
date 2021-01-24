package validation

import quests.Quest
import quests.QuestManager

class WeatherValidator {

    //TODO - use shared conditional String validator
    //No duplicate weather keys
    //All weathers exist
    private val quests = QuestManager.quests

    fun validate(): Int {
        return noDuplicateStages()
    }

    private fun noDuplicateStages(): Int {
        return quests.map { noDuplicateStages(it) }.sum()
    }

    private fun noDuplicateStages(quest: Quest): Int {
        val stages = mutableListOf<Int>()
        var warnings = 0
        quest.getAllEvents().forEach { storyEvent ->
            if (stages.contains(storyEvent.stage)) {
                println("WARN: Quest has multiple events at stage '${storyEvent.stage}'.")
                warnings++
            } else {
                stages.add(storyEvent.stage)
            }
        }
        return warnings
    }

}
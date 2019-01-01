package core.gameState.quests

import core.utility.NameSearchableList

interface QuestParser {

    fun parseQuests() : NameSearchableList<Quest>
}
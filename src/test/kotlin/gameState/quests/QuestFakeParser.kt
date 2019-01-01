package gameState.quests

import core.gameState.quests.Quest
import core.gameState.quests.QuestParser
import core.utility.NameSearchableList

class QuestFakeParser(val quests: List<Quest> = listOf()) : QuestParser {

    override fun parseQuests(): NameSearchableList<Quest> {
        return NameSearchableList(quests)
    }
}
package core.gameState.quests

import core.utility.NameSearchableList
import system.DependencyInjector

object QuestManager {
    private var parser = DependencyInjector.getImplementation(QuestParser::class.java)
    var quests = parser.parseQuests()

    fun getActiveQuests() : NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active})
    }

    fun getAllPlayerQuests() : NameSearchableList<Quest> {
        return NameSearchableList(quests.filter { it.active || it.complete})
    }

    fun reset(){
        parser = DependencyInjector.getImplementation(QuestParser::class.java)
        quests = parser.parseQuests()
    }

}
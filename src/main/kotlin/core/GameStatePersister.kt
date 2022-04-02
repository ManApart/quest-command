package core

import core.properties.PropertiesP
import quests.QuestManager
import quests.QuestP


@kotlinx.serialization.Serializable
data class GameStateP(
    val gameName: String,
    val quests: List<QuestP>,
    val time: Long,
    val properties: PropertiesP,
    val aliases: Map<String, String>,
) {
    constructor() : this(GameState.gameName, QuestManager.quests.filter { it.hasStarted() }.map { QuestP(it) }, GameState.timeManager.getTicks(), PropertiesP(GameState.properties), GameState.aliases)

    fun updateGameState(){
        GameState.gameName = gameName
        GameState.properties.replaceWith(properties.parsed())
        quests.forEach { it.updateQuestManager() }
        GameState.timeManager.setTime(time)
        GameState.aliases.clear()
        GameState.aliases.putAll(aliases)
    }
}
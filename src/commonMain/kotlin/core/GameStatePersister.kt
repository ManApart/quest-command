package core

import core.properties.PropertiesP
import quests.QuestManager
import quests.QuestP


@kotlinx.serialization.Serializable
data class GameStateP(
    val gameName: String,
    val characterNames: List<String> = emptyList(),
    val quests: List<QuestP> = emptyList(),
    val time: Long,
    val properties: PropertiesP = PropertiesP(),
    val aliases: Map<String, String> = emptyMap(),
) {
    constructor() : this(GameState.gameName, GameState.players.values.map { it.name }, QuestManager.quests.filter { it.hasStarted() }.map { QuestP(it) }, GameState.timeManager.getTicks(), PropertiesP(GameState.properties), GameState.aliases)

    fun updateGameState(){
        GameState.gameName = gameName
        GameState.properties.replaceWith(properties.parsed())
        quests.forEach { it.updateQuestManager() }
        GameState.timeManager.setTime(time)
        GameState.aliases.clear()
        GameState.aliases.putAll(aliases)
    }
}

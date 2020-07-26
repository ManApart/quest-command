package core

import quests.QuestManager
import quests.getPersisted
import quests.readFromData


fun getPersistedGameState(): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["gameName"] = GameState.gameName
    //TODO - move quests to their own file(s)
    data["quests"] = QuestManager.quests.filter { it.hasStarted() }.map { getPersisted(it) }
    data["time"] = GameState.timeManager.getTicks().toString()
    data["properties"] = core.properties.getPersisted(GameState.properties)
    data["aliases"] = GameState.aliases

//    if (GameState.battle != null) {
//        data["battle"] = GameState.battle
//    }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readGameStateFromData(data: Map<String, Any>) {
    GameState.gameName = data["gameName"] as String
    GameState.properties.replaceWith(core.properties.readFromData(data["properties"] as Map<String, Any>))
    (data["quests"] as List<Map<String, Any>>).forEach { readFromData(it) }
    GameState.timeManager.setTime((data["time"] as String).toLong())
    GameState.aliases.clear()
    GameState.aliases.putAll(data["aliases"] as Map<String, String>)
}
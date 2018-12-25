package core.gameState.dataParsing

import core.gameState.quests.QuestManager

object GamestateQuery {

    fun getValue(property: String, params: List<String>) : String {
        return when (property) {
            "QuestStage" -> QuestManager.quests.get(params[0]).stage.toString()
            else -> ""
        }
    }

}
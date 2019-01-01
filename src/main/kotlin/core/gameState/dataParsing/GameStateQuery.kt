package core.gameState.dataParsing

import core.gameState.quests.QuestManager
import core.history.display
import interact.scope.ScopeManager

object GameStateQuery {

    fun getValue(property: String, params: List<String>): String {
        return when (property) {
            "QuestStage" -> QuestManager.quests.get(params[0]).stage.toString()
            "Target" -> getTargetValue(params)
            else -> ""
        }
    }

    private fun getTargetValue(params: List<String>): String {
        if (params.size < 2) {
            display("Wrong number of params to query Target for: ${params.joinToString(", ")}")
            return ""
        }

        val target = ScopeManager.getScope().getTargetsIncludingPlayerInventory(params[0]).first()
        val type = params[1].toLowerCase()

        if (type != "tags" && params.size != 3) {
            display("Wrong number of params to query Target for: ${params.joinToString(", ")}")
            return ""
        }

        return when (type) {
            "values" -> target.properties.values.getString(params[2])
            "stats" -> target.properties.values.getString(params[2])
            "tags" -> target.properties.tags.getAll().joinToString(",")
            else -> ""
        }

    }
}
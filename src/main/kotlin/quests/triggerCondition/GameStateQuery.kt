package quests.triggerCondition

import core.history.display
import core.utility.RandomManager
import quests.QuestManager
import traveling.scope.ScopeManager

object GameStateQuery {

    fun getValue(property: String, params: List<String>): String {
        return when (property) {
            "QuestStage" -> QuestManager.quests.get(params[0]).stage.toString()
            "Location" -> getLocation(params)
            "Target" -> getTargetValues(params, false)
            "AllTargets" -> getTargetValues(params, true)
            "Chance" -> getChance(params)
            else -> ""
        }
    }

    private fun getChance(params: List<String>): String {
        val chance = params[0].toIntOrNull() ?: 50
        val success = RandomManager.isSuccess(chance / 100.00)
        return if (success) {
            "success"
        } else {
            "fail"
        }
    }

    private fun getLocation(params: List<String>): String {
        if (params.isEmpty()) {
            display("Wrong number of params to query Target for: ${params.joinToString(", ")}")
            return ""
        }
        return ScopeManager.getScope().getCreatures(params[0]).first().location.name
    }

    private fun getTargetValues(params: List<String>, all: Boolean): String {
        if (params.size < 2) {
            display("Wrong number of params to query Target for: ${params.joinToString(", ")}")
            return ""
        }

        val targets = if (all) {
            ScopeManager.getScope().getTargetsIncludingPlayerInventory(params[0])
        } else {
            listOf(ScopeManager.getScope().getTargetsIncludingPlayerInventory(params[0]).first())
        }

        val type = params[1].toLowerCase()

        if (type != "tags" && params.size != 3) {
            display("Wrong number of params to query Target for: ${params.joinToString(", ")}")
            return ""
        }

        val values = mutableListOf<String?>()
        targets.forEach { target ->
            values.add(when (type) {
                "values" -> target.properties.values.getString(params[2])
                "stats" -> target.properties.values.getString(params[2])
                "tags" -> target.properties.tags.getAll().joinToString(",")
                else -> null
            })
        }

        return values.asSequence().filterNotNull().joinToString(",")
    }

}
package validation

import core.ai.AIManager
import core.utility.toNameSearchableList


class DesireValidator {

    private val desires = AIManager.desires
    private val agendas = AIManager.agendas.toNameSearchableList()

    fun validate(): Int {
        return noGoalsWithoutAgendas()
    }

    private fun noGoalsWithoutAgendas(): Int {
        var warnings = 0
        desires.forEach { desireTree ->
            desireTree.getAllDesires().map { it.first }.forEach { desire ->
                if (agendas.getOrNull(desire) == null) {
                    println("WARN: Could not find agenda '${desire}' for desire '${desire}'.")
                    warnings++
                }
            }
        }
        return warnings
    }


}
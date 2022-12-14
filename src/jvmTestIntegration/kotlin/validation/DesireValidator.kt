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
        desires.forEach { desire ->
            if (agendas.getOrNull(desire.name) == null) {
                println("WARN: Could not find agenda '${desire.agenda}' for desire '${desire.name}'.")
                warnings++
            }
        }
        return warnings
    }


}
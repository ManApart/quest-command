package validation

import core.ai.AIManager


class DesireValidator {

    private val desires = AIManager.desires
    private val agendas = AIManager.agendas

    fun validate(): Int {
        return noGoalsWithoutAgendas() + noAgendaOrphans()
    }

    private fun noGoalsWithoutAgendas(): Int {
        var warnings = 0
        desires.forEach { desireTree ->
            desireTree.getAllDesires().map { it.first }.forEach { desire ->
                if (agendas[desire] == null) {
                    println("WARN: Could not find agenda '${desire}' for desire '${desire}'.")
                    warnings++
                }
            }
        }
        return warnings
    }
    private fun noAgendaOrphans(): Int {
        var warnings = 0
        agendas.values.forEach { agenda ->
            agenda.steps.mapNotNull { it.agendaName }.forEach { reference ->
                if (agendas[reference] == null){
                    println("WARN: Could not find agenda '${reference}' referenced by agenda '${agenda}'.")
                    warnings++
                }
            }
        }
        return warnings
    }


}
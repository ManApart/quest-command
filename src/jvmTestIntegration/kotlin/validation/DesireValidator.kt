package validation

import core.ai.AIManager
import core.ai.desire.DesireTree
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals


class DesireValidator {
    private val agendas = AIManager.agendas

    @Test
    fun validate() {
        val desires = runBlocking { AIManager.getDesires() }
        assertEquals(0, noGoalsWithoutAgendas(desires) + noAgendaOrphans())
    }

    private fun noGoalsWithoutAgendas(desires: List<DesireTree>): Int {
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
                if (agendas[reference] == null) {
                    println("WARN: Could not find agenda '${reference}' referenced by agenda '${agenda}'.")
                    warnings++
                }
            }
        }
        return warnings
    }


}

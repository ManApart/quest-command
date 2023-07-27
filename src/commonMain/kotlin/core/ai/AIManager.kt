package core.ai

import building.ModManager
import core.DependencyInjector
import core.ai.agenda.Agenda
import core.ai.agenda.AgendasCollection
import core.ai.desire.DesireTree
import core.ai.desire.DesiresCollection
import core.startupLog
import core.utility.Backer
import core.utility.lazyM

object AIManager {
    private val desires = Backer(::loadDesires)
    suspend fun getDesires() = desires.get()

    var agendas by lazyM { loadAgendas() }
        private set

    private suspend fun loadDesires(): List<DesireTree> {
        startupLog("Loading AI Desires.")
        return DependencyInjector.getImplementation(DesiresCollection::class).values() + ModManager.ai
    }

    private fun loadAgendas(): Map<String, Agenda> {
        startupLog("Loading AI Agendas.")
        return (DependencyInjector.getImplementation(AgendasCollection::class).values + ModManager.agendas).associateBy { it.name }
    }

    suspend fun reset() {
        desires.reset()
        agendas = loadAgendas()
    }

}
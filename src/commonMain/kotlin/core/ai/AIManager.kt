package core.ai

import core.DependencyInjector
import core.ai.agenda.Agenda
import core.ai.agenda.AgendasCollection
import core.ai.desire.DesireTree
import core.ai.desire.DesiresCollection
import core.startupLog
import core.utility.lazyM

object AIManager {
    var desires by lazyM { loadDesires() }
        private set
    var agendas by lazyM { loadAgendas() }
        private set

    private suspend fun loadDesires(): List<DesireTree> {
        startupLog("Loading AI Desires.")
        return DependencyInjector.getImplementation(DesiresCollection::class).values()
    }

    private fun loadAgendas(): Map<String, Agenda> {
        startupLog("Loading AI Agendas.")
        return DependencyInjector.getImplementation(AgendasCollection::class).values.associateBy { it.name }
    }

    fun reset() {
        desires = loadDesires()
        agendas = loadAgendas()
    }

}
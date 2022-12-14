package core.ai

import core.DependencyInjector
import core.ai.action.AIActionTree
import core.ai.action.dsl.AIActionsCollection
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
    //TODO - delete
    var actions by lazyM { loadActions() }
        private set

    private fun loadDesires(): List<DesireTree> {
        startupLog("Loading AI Desires.")
        return DependencyInjector.getImplementation(DesiresCollection::class).values
    }

    private fun loadAgendas(): List<Agenda> {
        startupLog("Loading AI Agendas.")
        return DependencyInjector.getImplementation(AgendasCollection::class).values
    }

    private fun loadActions(): List<AIActionTree> {
        startupLog("Loading AI Actions.")
        return DependencyInjector.getImplementation(AIActionsCollection::class).values
    }

    fun reset() {
        desires = loadDesires()
        agendas = loadAgendas()
        actions = loadActions()
    }

}
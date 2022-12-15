package core.ai.agenda

import core.ai.action.AIAction2
import core.conditional.Context
import core.events.Event
import core.thing.Thing

class AgendaBuilder(private val name: String) {
    var priority: Int? = null
    private val steps: MutableList<GoalStep2> = mutableListOf()

    fun action(name: String, result: ((Thing, Context) -> Event)) {
        this.steps.add(GoalStep2(AIAction2(name, { thing, context -> listOf(result(thing, context)) })))
    }

    fun agenda(name: String) {
        this.steps.add(GoalStep2(name))
    }

    internal fun build(): Agenda {
        return Agenda(name, steps)
    }

}


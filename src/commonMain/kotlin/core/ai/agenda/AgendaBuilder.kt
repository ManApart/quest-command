package core.ai.agenda

import core.ai.action.AIAction
import core.conditional.Context
import core.events.Event
import core.thing.Thing

class AgendaBuilder(private val name: String) {
    var priority: Int? = null
    private val steps: MutableList<GoalStep> = mutableListOf()

    fun action(name: String, result: (Thing, Context) -> Event?) {
        this.steps.add(GoalStep(AIAction(name, { thing, context ->
            result(thing, context)?.let { listOf(it) }
        })))
    }

    fun actions(name: String, result: (Thing, Context) -> List<Event>?) {
        this.steps.add(GoalStep(AIAction(name, result)))
    }

    fun agenda(name: String) {
        this.steps.add(GoalStep(name))
    }

    internal fun build(): Agenda {
        return Agenda(name, steps)
    }

}


package core.ai.agenda

import core.ai.action.AIAction
import core.events.Event
import core.thing.Thing

class AgendaBuilder(private val name: String) {
    private val steps: MutableList<GoalStep> = mutableListOf()

    fun action(name: String, result: suspend (Thing) -> Event?) {
        this.steps.add(GoalStep(AIAction(name, { thing ->
            result(thing)?.let { listOf(it) }
        })))
    }

    fun actionDetailed(name: String, initializer: ActionBuilder.() -> Unit) {
        this.steps.add(GoalStep(ActionBuilder(name).apply(initializer).build()))
    }

    fun actions(name: String, result: suspend (Thing) -> List<Event>?) {
        this.steps.add(GoalStep(AIAction(name, result)))
    }

    fun agenda(name: String) {
        this.steps.add(GoalStep(name))
    }

    internal fun build(): Agenda {
        return Agenda(name, steps)
    }

}


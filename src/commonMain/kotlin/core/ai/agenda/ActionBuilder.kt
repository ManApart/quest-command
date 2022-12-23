package core.ai.agenda

import core.ai.action.AIAction
import core.events.Event
import core.thing.Thing

class ActionBuilder(private val name: String) {
    private var result: (Thing) -> Event? = { null }
    private var resultList: ((Thing) -> List<Event>?)? = null
    private var shouldSkip: (Thing) -> Boolean? = { true }

    fun result(result: (Thing) -> Event?) {
        this.result = result
    }

    fun results(result: (Thing) -> List<Event>?) {
        this.resultList = result
    }

    fun shouldSkip(shouldSkip: (Thing) -> Boolean?) {
        this.shouldSkip = shouldSkip
    }

    internal fun build(): AIAction {
        val res = resultList ?: { thing ->
            result(thing)?.let { listOf(it) }
        }
        return AIAction(name, res, shouldSkip)
    }

}


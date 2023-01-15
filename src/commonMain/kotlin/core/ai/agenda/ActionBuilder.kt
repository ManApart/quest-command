package core.ai.agenda

import core.ai.action.AIAction
import core.events.Event
import core.thing.Thing

class ActionBuilder(private val name: String) {
    private var result: suspend (Thing) -> Event? = { null }
    private var resultList: (suspend (Thing) -> List<Event>?)? = null
    private var shouldSkip: suspend (Thing) -> Boolean? = { true }

    fun result(result: suspend (Thing) -> Event?) {
        this.result = result
    }

    fun results(result: suspend (Thing) -> List<Event>?) {
        this.resultList = result
    }

    fun shouldSkip(shouldSkip: suspend (Thing) -> Boolean?) {
        this.shouldSkip = shouldSkip
    }

    internal fun build(): AIAction {
        val res = resultList ?: { thing ->
            result(thing)?.let { listOf(it) }
        }
        return AIAction(name, res, shouldSkip)
    }

}


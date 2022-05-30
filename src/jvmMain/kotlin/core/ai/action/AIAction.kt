package core.ai.action

import core.events.Event
import core.events.EventManager
import core.thing.Thing
import core.utility.Named

class AIAction(
    override val name: String,
    private val conditions: List<(Thing) -> Boolean> = listOf(),
    private val createEvents: (Thing) -> List<Event> = { listOf()},
    val priority: Int = 10
) : Named {
    fun canRun(owner: Thing): Boolean {
        return conditions.all { it(owner) }
    }

    fun execute(owner: Thing) {
        createEvents(owner).forEach { EventManager.postEvent(it) }
    }
}
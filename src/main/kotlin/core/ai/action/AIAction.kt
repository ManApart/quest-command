package core.ai.action

import core.events.Event
import core.events.EventManager
import core.target.Target
import core.utility.Named

class AIAction(
    override val name: String,
    private val conditions: List<(Target) -> Boolean> = listOf(),
    private val createEvents: (Target) -> List<Event> = { listOf()},
    val priority: Int = 10
) : Named {
    fun canRun(owner: Target): Boolean {
        return conditions.all { it(owner) }
    }

    fun execute(owner: Target) {
        createEvents(owner).forEach { EventManager.postEvent(it) }
    }
}
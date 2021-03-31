package core.ai.action

import core.events.Event
import core.events.EventManager
import core.target.Target
import core.utility.Named

class AIAction(
    override val name: String,
    private val condition: (Target) -> Boolean = { true},
    private val createEvents: (Target) -> List<Event> = { listOf()},
) : Named {
    fun canRun(owner: Target): Boolean {
        return condition(owner)
    }

    fun execute(owner: Target) {
        createEvents(owner).forEach { EventManager.postEvent(it) }
    }
}
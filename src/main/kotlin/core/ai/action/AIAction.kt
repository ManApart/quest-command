package core.ai.action

import core.events.Event
import core.events.EventManager
import core.target.Target
import core.utility.Named

class AIAction(
    override val name: String,
    private val condition: (Target, Map<String, String>) -> Boolean = {_, _ -> true},
    private val createEvents: (Target, Map<String, String>) -> List<Event> = {_,_ -> listOf()},
    val params: Map<String, String> = mapOf()
) : Named {
    fun canRun(owner: Target): Boolean {
        return condition(owner, params)
    }

    fun execute(owner: Target) {
        createEvents(owner, params).forEach { EventManager.postEvent(it) }
    }
}
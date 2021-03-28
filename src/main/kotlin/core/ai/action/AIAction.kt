package core.ai.action

import core.events.Event
import core.events.EventManager
import core.target.Target

class AIAction(
    private val condition: (Target, Map<String, String>) -> Boolean = {_, _ -> true},
    private val createEvents: (Target, Map<String, String>) -> List<Event> = {_,_ -> listOf()},
    val params: Map<String, String> = mapOf()
) {
    fun canRun(owner: Target): Boolean {
        return condition(owner, params)
    }

    fun execute(owner: Target) {
        createEvents(owner, params).forEach { EventManager.postEvent(it) }
    }
}
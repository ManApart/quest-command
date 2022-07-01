package core.ai.action

import core.events.Event
import core.events.EventManager
import core.thing.Thing
import core.utility.Named

typealias Context =  Map<String, (Thing) -> Any?>

class AIAction(
    override val name: String,
    private val context: Context = mapOf(),
    private val conditions: List<(Thing, Context) -> Boolean> = listOf(),
    private val createEvents: (Thing, Context) -> List<Event> = { _, _ -> listOf()},
    val priority: Int = 10
) : Named {

    fun canRun(owner: Thing): Boolean {
        return conditions.all { it(owner, context) }
    }

    fun execute(owner: Thing) {
        createEvents(owner, context).forEach { EventManager.postEvent(it) }
    }
}
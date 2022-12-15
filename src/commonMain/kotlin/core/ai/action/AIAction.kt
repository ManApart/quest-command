package core.ai.action

import core.conditional.Context
import core.events.Event
import core.events.EventManager
import core.thing.Thing
import core.utility.Named

data class AIAction(
    override val name: String,
    private val context: Context = Context(),
    private val createEvents: (Thing, Context) -> List<Event> = { _, _ -> listOf() },
    val priority: Int = 10
) : Named {

    fun execute(owner: Thing) {
        try {
            createEvents(owner, context).forEach { EventManager.postEvent(it) }
        } catch (e: Exception){
            println("Failed to create event actions for ${owner.name}. This shouldn't happen!")
            println(e.message)
            e.printStackTrace()
        }
    }
}

data class AIAction2(
    override val name: String,
    private val createEvents: (Thing, Context) -> List<Event>? = { _, _ -> listOf() },
    val optional: Boolean = false //Could make this a function based on what's current etc
) : Named
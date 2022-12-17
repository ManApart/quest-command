package core.ai.action

import core.conditional.Context
import core.events.Event
import core.events.EventManager
import core.thing.Thing
import core.utility.Named

data class AIAction2(
    override val name: String,
    val createEvents: (Thing, Context) -> List<Event>? = { _, _ -> listOf() },
    val optional: Boolean = false //Could make this a function based on what's current etc
) : Named
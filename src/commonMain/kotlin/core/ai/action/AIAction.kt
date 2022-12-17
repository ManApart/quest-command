package core.ai.action

import core.conditional.Context
import core.events.Event
import core.thing.Thing
import core.utility.Named

data class AIAction(
    override val name: String,
    val createEvents: (Thing, Context) -> List<Event>? = { _, _ -> listOf() },
    val optional: Boolean = false //TODO Could make this a function based on what's current etc
) : Named
package core.ai.action

import core.events.Event
import core.thing.Thing
import core.utility.Named

data class AIAction(
    override val name: String,
    val createEvents: suspend (Thing) -> List<Event>? = { _ -> listOf() },
    val shouldSkip: suspend (Thing) -> Boolean? = { false }
) : Named
package core.ai.action

import core.events.Event
import core.thing.Thing
import core.utility.Named

data class AIAction(
    override val name: String,
    val createEvents: (Thing) -> List<Event>? = { _ -> listOf() },
    val shouldSkip: (Thing) -> Boolean? = { false }
) : Named
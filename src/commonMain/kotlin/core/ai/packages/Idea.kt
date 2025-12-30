package core.ai.packages

import core.events.Event
import core.thing.Thing
import use.interaction.nothing.NothingEvent


val DO_NOTHING_IDEA = Idea("Do Nothing", 0, { true }, { listOf(NothingEvent(it)) })

data class Idea(val name: String, val priority: Int, val criteria: suspend (Thing) -> Boolean, val action: suspend (Thing) -> List<Event>)

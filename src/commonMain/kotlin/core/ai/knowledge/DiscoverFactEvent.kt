package core.ai.knowledge

import core.events.Event
import core.thing.Thing

data class DiscoverFactEvent(val source: Thing, val fact: Fact) : Event

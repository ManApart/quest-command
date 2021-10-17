package core.ai.action.dsl

import core.events.Event
import core.thing.Thing

class ProtoAction(val name: String, val createEvents: (Thing) -> List<Event>)

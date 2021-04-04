package core.ai.action.dsl

import core.events.Event
import core.target.Target

class ProtoAction(val name: String, val createEvents: (Target) -> List<Event>)

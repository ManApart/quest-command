package core.ai.action.dsl

import core.ai.action.Context
import core.events.Event
import core.thing.Thing

class ActionRecipe(val name: String, val createEvents: (Thing, Context) -> List<Event>)

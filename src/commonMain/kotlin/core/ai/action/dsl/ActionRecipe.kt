package core.ai.action.dsl

import core.conditional.Context
import core.events.Event
import core.thing.Thing

class ActionRecipe(val name: String, val createEvents: (Thing, Context) -> List<Event>)

class ActionRecipe2(val name: String, val createEvents: (Thing, Context) -> List<Event>)

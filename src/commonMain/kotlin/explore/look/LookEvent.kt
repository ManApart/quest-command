package explore.look

import core.Player
import core.body.Body2
import core.body.BodyPart
import core.events.Event
import core.thing.Thing

data class LookEvent(val source: Player, val thing: Thing? = null, val body: Body2? = null, val part: BodyPart? = null) : Event

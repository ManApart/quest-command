package explore.examine

import core.Player
import core.ai.knowledge.Mind
import core.body.Body
import core.body.BodyPart
import core.events.Event
import core.thing.Thing

data class ExamineEvent(val source: Player, val thing: Thing? = null, val body: Body? = null, val part: BodyPart? = null, val mind: Mind? = null) : Event

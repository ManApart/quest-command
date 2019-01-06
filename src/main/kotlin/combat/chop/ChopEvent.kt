package combat.chop

import combat.TargetDirection
import core.events.Event
import core.gameState.body.BodyPart
import core.gameState.Creature
import core.gameState.Target

class ChopEvent(val source: Creature, val sourcePart: BodyPart, val target: Target, val direction: TargetDirection) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}
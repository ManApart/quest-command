package combat.crush

import combat.TargetDirection
import core.events.Event
import core.gameState.bodies.BodyPart
import core.gameState.Creature
import core.gameState.Target

class CrushEvent(val source: Creature, val sourcePart: BodyPart, val target: Target, val direction: TargetDirection) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}
package battle.chop

import core.events.Event
import core.gameState.BodyPart
import core.gameState.Creature
import core.gameState.Item
import core.gameState.Target

class ChopEvent(val source: Creature, val sourcePart: BodyPart, val target: Target) : Event
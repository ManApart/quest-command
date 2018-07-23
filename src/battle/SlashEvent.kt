package battle

import core.events.Event
import core.gameState.BodyPart
import core.gameState.Creature
import core.gameState.Item
import core.gameState.Target

class SlashEvent(val source: Creature, val sourcePart: BodyPart, val target: Target) : Event
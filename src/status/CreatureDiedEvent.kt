package status

import core.events.Event
import core.gameState.Creature

class CreatureDiedEvent(val creature: Creature) : Event
package status

import core.events.Event
import core.gameState.Target

class CreatureDiedEvent(val creature: Target) : Event
package status.effects

import core.events.Event
import core.gameState.Creature
import core.gameState.Effect

class RemoveEffectEvent(val creature: Creature, val effect: Effect) : Event
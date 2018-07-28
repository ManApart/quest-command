package status.effects

import core.events.Event
import core.gameState.Effect
import core.gameState.Target

class RemoveEffectEvent(val target: Target, val effect: Effect) : Event
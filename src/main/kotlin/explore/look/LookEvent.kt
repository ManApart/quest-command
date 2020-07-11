package explore.look

import core.GameState
import core.events.Event
import core.target.Target

class LookEvent(val source: Target = GameState.player, val target: Target? = null) : Event
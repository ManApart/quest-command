package interact.scope

import core.events.Event
import core.gameState.Activator

class SpawnActivatorEvent(val activator: Activator, val silent: Boolean = false) : Event
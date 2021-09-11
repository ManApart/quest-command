package core.properties.propValChanged

import core.GameState
import core.events.Event
import core.target.Target

class PropertyStatChangeEvent(val target: Target = GameState.player, val sourceOfChange: String, val statName: String, val amount: Int, val silent: Boolean = false) : Event
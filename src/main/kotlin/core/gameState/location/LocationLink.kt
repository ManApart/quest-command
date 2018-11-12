package core.gameState.location

import core.gameState.NO_POSITION
import core.gameState.Position

class LocationLink(val name: String, val position: Position = NO_POSITION, val restricted: Boolean = false)
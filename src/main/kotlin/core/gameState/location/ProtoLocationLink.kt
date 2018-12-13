package core.gameState.location

import core.gameState.NO_POSITION
import core.gameState.Position

class ProtoLocationLink(val name: String, val position: Position = NO_POSITION, var restricted: Boolean = false, val oneWay: Boolean = false)
package core.gameState.location

class LocationTarget(val name: String, val location: String? = null) {
    constructor(targetName: String) : this(targetName, null)
}
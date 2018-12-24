package core.gameState.location

class LocationTarget(val name: String, val location: String? = null, val params: Map<String, String> = mapOf()) {
    constructor(targetName: String) : this(targetName, null, mapOf())
}
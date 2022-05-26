package core.ai.knowledge

import traveling.location.location.LocationManager

@kotlinx.serialization.Serializable
data class SubjectP(
    val name: String,
    val thingName: String?,
    val thingLocationName: String?,
    val thingNetworkName: String?,
    val locationName: String?,
    val locationNetworkName: String?,
    val propertyValue: String?,
    val propertyTag: String?
) {
    constructor(b: Subject) : this(b.name, b.thing?.name, b.thing?.location?.name, b.thing?.location?.network?.name, b.location?.name, b.location?.network?.name, b.propertyValue, b.propertyTag)

    fun parsed(): Subject {
        val thing = if (thingName != null && thingLocationName != null && thingNetworkName != null) {
            LocationManager.getNetwork(thingNetworkName).findLocation(thingLocationName).getLocation().getThings(thingName).firstOrNull()
        } else null

        val location = if (locationName != null && locationNetworkName != null) {
            LocationManager.getNetwork(locationNetworkName).findLocation(locationName)
        } else null

        return Subject(name, thing, location, propertyValue, propertyTag)
    }

}
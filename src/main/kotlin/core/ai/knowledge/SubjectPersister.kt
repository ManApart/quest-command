package core.ai.knowledge

import traveling.location.location.LocationManager

@kotlinx.serialization.Serializable
data class SubjectP(
    val thingName: String?,
    val locationName: String?,
    val networkName: String?,
    val topic: String?,
    val propertyValue: String?,
    val propertyTag: String?
) {
    constructor(b: Subject) : this(b.thing?.name, b.location?.name, b.location?.network?.name, b.topic, b.propertyValue, b.propertyTag)

    fun parsed(): Subject {
        val thing = if (thingName != null && locationName != null && networkName != null) {
            LocationManager.getNetwork(networkName).findLocation(locationName).getLocation().getThings(thingName).firstOrNull()
        } else null

        val location = if (locationName != null && networkName != null) {
            LocationManager.getNetwork(networkName).findLocation(locationName)
        } else null

        return Subject(thing, location, topic, propertyValue, propertyTag)
    }

}
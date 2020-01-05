package traveling.location

import traveling.location.location.LocationManager

fun getPersisted(dataObject: Network): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["name"] = dataObject.name
    data["locations"] = dataObject.getLocations().map { traveling.location.location.getPersisted(it) }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>): Network {
    val network = LocationManager.getNetwork(data["name"] as String)
    val locations = (data["locations"] as List<Map<String, Any>>).map { traveling.location.location.readFromData(it) }
    network.replaceLocations(locations)

    return network
}
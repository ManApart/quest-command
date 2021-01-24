package core.utility

import core.target.Target
import traveling.location.location.LocationManager
import traveling.location.location.LocationNode

//TODO - move all helper functions somewhere else
fun parseLocation(params: Map<String, String>, parent: Target, networkNameKey: String, locationNameKey: String): LocationNode {
    val network = if (params.containsKey(networkNameKey)) {
        LocationManager.getNetwork(params[networkNameKey]!!)
    } else {
        parent.location.network
    }
    if (params.containsKey(locationNameKey)) {
        if (network.locationNodeExists(params[locationNameKey]!!)) {
            return network.getLocationNode(params[locationNameKey]!!)
        }
    }
    return parent.location
}
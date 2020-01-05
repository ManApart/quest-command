package validation

import traveling.location.location.LocationManager

class LocationValidator {

    fun validate(): Int {
        return noDuplicateLocationNodeNames() +
                noDuplicateLocationNames()
    }

    private fun noDuplicateLocationNodeNames(): Int {
        var warnings = 0
        LocationManager.getNetworks().forEach { network ->
            val names = mutableListOf<String>()
            network.getLocationNodes().forEach { locationNode ->
                if (names.contains(locationNode.name)) {
                    println("WARN: Location Node '${locationNode.name}' has a duplicate name for network ${network.name}.")
                    warnings++
                } else {
                    names.add(locationNode.name)
                }
            }
        }
        return warnings
    }

    private fun noDuplicateLocationNames(): Int {
        var warnings = 0
        LocationManager.getNetworks().forEach { network ->
            val names = mutableListOf<String>()
            network.getLocations().forEach { location ->
                if (names.contains(location.name)) {
                    println("WARN: Location '${location.name}' has a duplicate name in network '${network.name}'.")
                    warnings++
                } else {
                    names.add(location.name)
                }
            }
        }
        return warnings
    }

}
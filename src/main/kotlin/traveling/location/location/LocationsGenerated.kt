package traveling.location.location

class LocationsGenerated : LocationsCollection {
    override val values = listOf<LocationResource>(resources.traveling.location.location.CommonLocations()).flatMap { it.values }
}
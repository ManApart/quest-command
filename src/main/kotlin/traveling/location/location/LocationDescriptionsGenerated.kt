package traveling.location.location

class LocationDescriptionsGenerated : LocationDescriptionsCollection {
    override val values = listOf<LocationDescriptionResource>(resources.locationDescriptions.LocationDescriptions()).flatMap { it.values }
}
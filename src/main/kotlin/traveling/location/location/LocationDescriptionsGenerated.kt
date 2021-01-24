package traveling.location.location
import core.conditional.ConditionalString

class LocationDescriptionsGenerated : LocationDescriptionsCollection {
    override val values: List<ConditionalString> = listOf(resources.locationDescriptions.LocationDescriptions()).flatMap { it.values }
}
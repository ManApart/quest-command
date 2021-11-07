package traveling.location.network

class NetworksGenerated : NetworksCollection {
    override val values = listOf<NetworkResource>(resources.traveling.location.location.KanbaraCity(), resources.traveling.location.location.KanbaraCountryside()).flatMap { it.values }
}
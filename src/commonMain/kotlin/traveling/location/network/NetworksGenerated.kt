package traveling.location.network

class NetworksGenerated : NetworksCollection {
    override val values by lazy { listOf<NetworkResource>(resources.traveling.location.location.KanbaraCity(), resources.traveling.location.location.KanbaraCountryside()).flatMap { it.values }}
}
package traveling.location.location

class NetworksGenerated : NetworksCollection {
    override val values = listOf<NetworkResource>(resources.traveling.location.location.CommonNetworks()).flatMap { it.values }
}
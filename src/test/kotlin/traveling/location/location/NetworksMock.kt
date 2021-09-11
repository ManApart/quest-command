package traveling.location.location

import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK
import traveling.location.network.NetworkBuilder
import traveling.location.network.NetworksCollection
import traveling.location.network.networks

class NetworksMock(override val values: List<NetworkBuilder> = networks {
    network(PLAYER_START_NETWORK){
        locationNode(PLAYER_START_LOCATION)
    }
}
) : NetworksCollection
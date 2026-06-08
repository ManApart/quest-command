package traveling.location.network

import traveling.location.network.NetworkStrings.PLAYER_START_LOCATION
import traveling.location.network.NetworkStrings.PLAYER_START_NETWORK


class NetworksMock(override val values: List<NetworkBuilder> = networks {
    network(PLAYER_START_NETWORK){
        locationNode(PLAYER_START_LOCATION)
    }
}
) : NetworksCollection

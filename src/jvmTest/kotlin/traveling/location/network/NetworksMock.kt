package traveling.location.network

import core.PLAYER_START_LOCATION
import core.PLAYER_START_NETWORK

class NetworksMock(override val values: List<NetworkBuilder> = networks {
    network(PLAYER_START_NETWORK){
        locationNode(PLAYER_START_LOCATION)
    }
}
) : NetworksCollection
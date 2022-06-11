package core.body

import traveling.location.network.NetworkBuilder

interface BodyResource {
    val values: List<NetworkBuilder>
}
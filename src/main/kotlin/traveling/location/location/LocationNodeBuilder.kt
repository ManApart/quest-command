package traveling.location.location

import traveling.location.Network
import traveling.location.ProtoConnectionBuilder

class LocationNodeBuilder(private val name: String) {
    private var locationName: String? = null
    private var parentName: String? = null
    private var network: Network? = null
    private var isRoot = false
    private val connectionBuilders = mutableListOf<ProtoConnectionBuilder>()

    internal fun build(): LocationNode {
        val locName = locationName ?: name
        val parent = parentName ?: DEFAULT_NETWORK.name
        val network = this.network ?: DEFAULT_NETWORK
        val connections = connectionBuilders.map { it.build() }
        return LocationNode(name, locName, parent, network, isRoot, connections)
    }

    fun location(name: String){
        this.locationName = name
    }

    fun parent(parent: String){
        this.parentName = parent
    }

    fun network(network: Network){
        this.network = network
    }

    fun isRoot(yes: Boolean){
        isRoot = yes
    }

    fun connection(name: String){
        connectionBuilders.add(ProtoConnectionBuilder().apply{name(name)})
    }

    fun connection(initializer: ProtoConnectionBuilder.() -> Unit) {
        connectionBuilders.add(ProtoConnectionBuilder().apply(initializer))
    }

}

fun node(name: String, initializer: LocationNodeBuilder.() -> Unit): LocationNode {
    return LocationNodeBuilder(name).apply(initializer).build()
}
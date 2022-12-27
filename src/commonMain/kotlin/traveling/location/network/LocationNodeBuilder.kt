package traveling.location.network

import traveling.location.ConnectionRecipeBuilder
import traveling.location.Network
import traveling.location.location.LocationRecipe
import traveling.location.location.LocationRecipeBuilder
import traveling.location.location.unBuild

class LocationNodeBuilder(private val name: String) {
    private var locationName: String? = null
    private var parentName: String? = null
    private var network: Network? = null
    private var isRoot = false
    private var recipe: LocationRecipeBuilder? = null
    private val connectionBuilders = mutableListOf<ConnectionRecipeBuilder>()

    internal fun build(recipes: Map<String, LocationRecipe>, inheritedParent: String? = null): LocationNode {
        val locName = locationName ?: name
        val parent = inheritedParent ?: parentName ?: DEFAULT_NETWORK.name
        val network = this.network ?: DEFAULT_NETWORK
        val connections = connectionBuilders.map { it.build() }
        val usedRecipe = if (recipe != null){
            recipe!!.build(listOf(recipes[locName]?.unBuild() ?: LocationRecipeBuilder(name)))
        } else {
            recipes[locName] ?: LocationRecipe(locName)
        }
        return LocationNode(name, locName, parent, network, isRoot, usedRecipe, connections)
    }

    fun location(name: String) {
        this.locationName = name
    }

    fun location(name: String, initializer: LocationRecipeBuilder.() -> Unit = {}) {
        recipe = LocationRecipeBuilder(name).apply(initializer)
    }

    fun material(material: String) {
        recipe = LocationRecipeBuilder(name).apply { material(material) }
    }

    fun parent(parent: String) {
        this.parentName = parent
    }

    fun network(network: Network) {
        this.network = network
    }

    fun isRoot(yes: Boolean) {
        isRoot = yes
    }

    fun connection(name: String, x: Int = 0, y: Int = 0, z: Int = 0) {
        connectionBuilders.add(ConnectionRecipeBuilder().apply {
            name(name)
            origin(x, y, z)
        })
    }

    fun connection(initializer: ConnectionRecipeBuilder.() -> Unit) {
        connectionBuilders.add(ConnectionRecipeBuilder().apply(initializer))
    }

    fun connection(name: String, initializer: ConnectionRecipeBuilder.() -> Unit) {
        connectionBuilders.add(ConnectionRecipeBuilder().apply { name(name) }.apply(initializer))
    }

}
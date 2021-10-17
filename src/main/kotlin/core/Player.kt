package core

import core.target.Target
import core.utility.NameSearchableList
import crafting.Recipe
import traveling.location.Route
import traveling.location.network.LocationNode

data class Player(
    val id: Int,
    val target: Target
) {
    //Map of Network to Location Node Name. Presence means it's discovered
    val knownLocations = mutableMapOf<String, MutableSet<String>>()
    val knownRecipes: NameSearchableList<Recipe> = NameSearchableList()
    var compassRoute: Route? = null

    override fun equals(other: Any?): Boolean {
        return other is Player && id == other.id
    }

    override fun hashCode(): Int {
        return id
    }

    fun isPlayer(): Boolean {
        return target.isPlayer()
    }

    val soul get() = target.soul
    val body get() = target.body
    val properties get() = target.properties
    val ai get() = target.ai
    val inventory get() = target.inventory
    val location get() = target.location
    val position get() = target.position

    fun discover(location: LocationNode){
        val network = location.network.name
        knownLocations.putIfAbsent(network, mutableSetOf())
        knownLocations[network]?.add(location.name)
    }

    fun knows(location: LocationNode): Boolean{
        val network = location.network.name
        return knownLocations[network]?.contains(location.name) ?: false
    }
}

package core

import core.thing.Thing
import core.utility.NameSearchableList
import crafting.Recipe
import traveling.location.Route
import traveling.location.network.LocationNode

data class Player(
    val name: String,
    val thing: Thing
) {
    //Map of Network to Location Node Name. Presence means it's discovered
    val knownLocations = mutableMapOf<String, MutableSet<String>>()
    val knownRecipes: NameSearchableList<Recipe> = NameSearchableList()
    var compassRoute: Route? = null

    override fun equals(other: Any?): Boolean {
        return other is Player && name == other.name
    }

    override fun hashCode(): Int {
        return name.length
    }

    fun isPlayer(): Boolean {
        return thing.isPlayer()
    }

    val soul get() = thing.soul
    val body get() = thing.body
    val properties get() = thing.properties
    val ai get() = thing.ai
    val inventory get() = thing.inventory
    val location get() = thing.location
    val position get() = thing.position

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

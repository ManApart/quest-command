package traveling.location.network

import traveling.location.location.LocationRecipe

class NetworksBuilder {
    internal val children = mutableListOf<NetworkBuilder>()

    fun network(item: NetworkBuilder) {
        children.add(item)
    }

    fun network(name: String, initializer: NetworkBuilder.() -> Unit = {}) {
        children.add(NetworkBuilder(name).apply(initializer))
    }
}

fun networks(initializer: NetworksBuilder.() -> Unit): List<NetworkBuilder> {
    return NetworksBuilder().apply(initializer).children
}

fun List<NetworkBuilder>.build(recipes: Map<String, LocationRecipe>): List<LocationNode> {
    return flatMap { it.build(recipes) }
}
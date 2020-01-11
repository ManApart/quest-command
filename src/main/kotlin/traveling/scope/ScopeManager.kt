package traveling.scope

import core.GameState
import system.persistance.save
import traveling.location.location.LocationNode

object ScopeManager {
    private val scopes = mutableMapOf<LocationNode, Scope>()

    init {
        reset()
    }

    fun reset() {
        scopes.clear()
    }

    fun getScope(targetLocation: LocationNode? = null) : Scope {
        val location = targetLocation ?: GameState.player.location
        //new scope should first check saved location
        if (!scopes.containsKey(location)){
            scopes[location] = Scope(location)
        }
        return scopes[location]!!
    }

    fun flush() {
        scopes.entries.forEach {
            save(GameState.gameName, it.key.network.name, it.key.name, it.value)
            scopes.remove(it.key)
        }
    }

}
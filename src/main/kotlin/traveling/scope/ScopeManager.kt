package traveling.scope

import core.AUTO_LOAD
import core.GameState
import system.persistance.loadScope
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

    fun getScope(targetLocation: LocationNode? = null): Scope {
        val location = targetLocation ?: GameState.player.location
        if (!scopes.containsKey(location)) {
            scopes[location] = getNewScope(location)
        }
        return scopes[location]!!
    }

    private fun getNewScope(locationNode: LocationNode): Scope {
        return if (GameState.properties.values.getBoolean(AUTO_LOAD)) {
            loadScope(GameState.gameName, locationNode)
        } else {
            Scope(locationNode)
        }
    }

    fun flush() {
        scopes.entries.toList().forEach {
            save(GameState.gameName, it.value)
            if (GameState.player.location != it.key) {
                scopes.remove(it.key)
            }
        }
    }

}
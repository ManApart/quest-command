package validation

import core.thing.activator.ActivatorManager
import kotlinx.coroutines.runBlocking

//TODO - check that connections with connection networks exist

class ActivatorValidator {

    private val activators = runBlocking { ActivatorManager.getAll() }

    fun validate(): Int {
        return noDuplicateNames()

    }

    private fun noDuplicateNames(): Int {
        var warnings = 0
        val names = mutableListOf<String>()
        activators.forEach { activator ->
            if (names.contains(activator.name)) {
                println("WARN: Activator '${activator.name}' has a duplicate name.")
                warnings++
            } else {
                names.add(activator.name)
            }
        }
        return warnings
    }


}
package validation

import system.ActivatorManager
import system.BehaviorManager
import system.location.LocationManager
import travel.climb.ClimbPathManager

class ActivatorValidator {

    private val activators = ActivatorManager.getAll()

    fun validate(): Int {
        return noDuplicateNames() +
                behaviorsExist() +
                climbPathsExist() +
                climbDestinationsExist()

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

    private fun behaviorsExist(): Int {
        var warnings = 0
        activators.forEach { activator ->
            activator.behaviorRecipes.forEach { recipe ->
                if (!BehaviorManager.behaviorExists(recipe)) {
                    println("WARN: Activator '${activator.name}' references nonexistent behavior: ${recipe.name}.")
                    warnings++
                }
            }
        }
        return warnings
    }

    private fun climbPathsExist(): Int {
        var warnings = 0
        activators.forEach { activator ->
            if (activator.climb != null) {
                if (!ClimbPathManager.pathExists(activator.climb!!.name)) {
                    println("WARN: Activator '${activator.name}' references nonexistent path: ${activator.climb!!.name}.")
                    warnings++
                }
            }
        }
        return warnings
    }

    private fun climbDestinationsExist(): Int {
        var warnings = 0
        activators.forEach { activator ->
            if (activator.climb != null) {
                if (!activator.climb!!.destinationName.startsWith("\$") && activator.climb!!.destination == LocationManager.NOWHERE_NODE) {
                    println("WARN: Activator '${activator.name}' references nonexistent climb path destination.")
                    warnings++
                }
            }
        }
        return warnings
    }

}
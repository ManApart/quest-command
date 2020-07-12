package explore.look

import core.GameState
import core.history.display
import core.target.targetsToString
import traveling.position.NO_VECTOR
import traveling.scope.getHeatLevel
import traveling.scope.getLightLevel

fun describeLocation() {
    val pos = GameState.player.position
    if (pos == NO_VECTOR) {
        display("You are at ${GameState.player.location.name}")
    } else {
        display("You are at ${pos.x}, ${pos.y}, ${pos.z} of ${GameState.player.location.name}")
    }
    if (GameState.currentLocation().getTargets().size > 1) {
        val targetList = targetsToString(GameState.currentLocation().getTargets().filterNot { it == GameState.player })
        display("You find yourself surrounded by $targetList.")
    } else {
        display("You don't see anything of use.")
    }
}

fun describeLocationDetailed() {
    val pos = GameState.player.position
    val locationRecipe = GameState.player.location.getLocationRecipe()
    val location = GameState.currentLocation()
    if (pos == NO_VECTOR) {
        display("You are at ${GameState.player.location.name}")
    } else {
        display("You are at ${pos.x}, ${pos.y}, ${pos.z} of ${GameState.player.location.name}")
    }
    display(locationRecipe.getDescription())
    display(location.weather.description)
    val light = getLightLevel(location)
    val heat = getHeatLevel(location)
    display("It is $light light and $heat hot.")
    if (location.getTargets().size > 1) {
        val targetList = targetsToString(location.getTargets().filterNot { it == GameState.player })
        display("You find yourself surrounded by $targetList.")
    } else {
        display("You don't see anything of use.")
    }
}
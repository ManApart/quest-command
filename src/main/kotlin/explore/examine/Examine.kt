package explore.examine

import core.GameState
import core.events.EventListener
import core.history.display
import core.properties.IS_CLIMBING
import core.target.Target
import core.target.targetsToString
import traveling.climb.ClimbLook
import traveling.position.NO_VECTOR
import traveling.scope.getHeatLevel
import traveling.scope.getLightLevel

class Examine : EventListener<ExamineEvent>() {


    override fun execute(event: ExamineEvent) {
        when {
            GameState.player.properties.values.getBoolean(IS_CLIMBING) -> ClimbLook.describeClimbJourney()
            event.target != null -> describeTarget(event.target)
            event.source.ai.aggroTarget != null -> describeBattle()
            else -> describeLocation()
        }
    }

    private fun describeTarget(target: Target) {
        var message = target.getDisplayName()
        message += "\n\t${target.getDescription()}"
        message += describeStatusEffects(target)
        message += describeWeight(target)
        message += describeProperties(target)
        display(message)
    }

    private fun describeStatusEffects(target: Target): String {
        if (target.soul.getConditions().isNotEmpty()) {
            val effects = target.soul.getConditions().joinToString(", ") { it.name }
            return "\n\t${target.name} is $effects"
        }
        return ""
    }

    private fun describeProperties(target: Target): String {
        if (!target.properties.isEmpty()) {
            return "\n\t${target.properties}"
        }
        return ""
    }

    private fun describeWeight(target: Target): String {
        return if (target.getWeight() > 0) {
            "\n\tWeight: ${target.getWeight()}"
        } else {
            ""
        }
    }

    private fun describeLocation() {
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




}
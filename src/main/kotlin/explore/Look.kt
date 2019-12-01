package explore

import core.events.EventListener
import core.gameState.GameState
import core.gameState.NO_VECTOR
import core.gameState.Target
import core.gameState.targetsToString
import core.history.display
import interact.scope.ScopeManager

class Look : EventListener<LookEvent>() {


    override fun execute(event: LookEvent) {
        when {
            GameState.player.isClimbing -> ClimbLook.describeClimbJourney()
            event.target != null -> describeTarget(event.target)
            GameState.battle != null -> GameState.battle?.describe()
            else -> describeLocation()
        }
    }

    private fun describeTarget(target: Target) {
        var message = target.getDisplayName()
        message += "\n\t${target.description}"
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
        if (pos == NO_VECTOR) {
            display("You are at ${GameState.player.location.getDescription()}")
        } else {
            display("You are at ${pos.x}, ${pos.y}, ${pos.z} of ${GameState.player.location.getDescription()}")
        }
        if (ScopeManager.getScope().getTargets().size > 1) {
            val targetList = targetsToString(ScopeManager.getScope().getTargets().filterNot { it == GameState.player })
            display("You find yourself surrounded by $targetList.")
        } else {
            display("You don't see anything of use.")
        }
    }




}